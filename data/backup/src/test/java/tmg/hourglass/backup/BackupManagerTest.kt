package tmg.hourglass.backup

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.TagRepository
import tmg.testutils.BaseTest
import java.time.LocalDateTime

internal class BackupManagerTest : BaseTest() {

    private val countdownRepository: CountdownRepository = mockk(relaxed = true)
    private val tagRepository: TagRepository = mockk(relaxed = true)

    private val underTest = BackupManager(
        countdownRepository = countdownRepository,
        tagRepository = tagRepository
    )

    @Test
    fun `generateBackupJSON produces valid v1 JSON string`() = runTest {
        val tag = Tag(
            tagId = "tag_1",
            name = "Work",
            colour = "#FF0000",
            sort = TagOrdering.ALPHABETICAL,
            expanded = true
        )
        val countdown = Countdown.Static(
            id = "cd_1",
            name = "Project Launch",
            description = "Launch date",
            colour = "#00FF00",
            emoji = "🚀",
            start = "2026-01-01",
            end = "2026-12-31",
            startValue = "0",
            endValue = "100",
            countdownType = CountdownType.DAYS,
            tag = tag,
            notifications = listOf(CountdownNotifications.AtValue("n1", "50"))
        )

        coEvery { tagRepository.getAll() } returns flowOf(listOf(tag))
        coEvery { countdownRepository.all() } returns flowOf(listOf(countdown))

        val jsonResult = underTest.generateBackupJSON()

        assertTrue(jsonResult.contains("\"schemaVersion\": \"v1\""))
        assertTrue(jsonResult.contains("\"tag_1\""))
        assertTrue(jsonResult.contains("\"cd_1\""))
        assertTrue(jsonResult.contains("\"Work\""))
    }

    @Test
    fun `restoreBackupJSON with v1 schema returns true and restores data`() = runTest {
        val v1Json = """
            {
                "schemaVersion": "v1",
                "tags": [
                    {
                        "id": "t1",
                        "name": "Personal",
                        "colour": "#123456",
                        "sort": "ALPHABETICAL",
                        "expanded": false
                    }
                ],
                "countdowns": [
                    {
                        "id": "c1",
                        "name": "Holiday",
                        "description": "Trip",
                        "colour": "#654321",
                        "start": "2026-06-01",
                        "end": "2026-06-15",
                        "initial": "0",
                        "finishing": "100",
                        "passageType": "DAYS",
                        "isRecurring": false,
                        "interpolator": "LINEAR",
                        "tagId": "t1",
                        "emoji": "✈️",
                        "notifications": []
                    }
                ]
            }
        """.trimIndent()

        val success = underTest.restoreBackupJSON(v1Json)

        assertTrue(success)
        coVerify(exactly = 1) { tagRepository.insertTag(any()) }
        coVerify(exactly = 1) { countdownRepository.saveAll(any()) }
    }

    @Test
    fun `restoreBackupJSON with unknown version returns false`() = runTest {
        val unknownJson = """
            {
                "schemaVersion": "v999",
                "tags": [],
                "countdowns": []
            }
        """.trimIndent()

        val success = underTest.restoreBackupJSON(unknownJson)

        assertFalse(success)
        coVerify(exactly = 0) { tagRepository.insertTag(any()) }
        coVerify(exactly = 0) { countdownRepository.saveAll(any()) }
    }

    @Test
    fun `restoreBackupJSON with malformed json returns false`() = runTest {
        val malformedJson = "{ invalid json }"

        val success = underTest.restoreBackupJSON(malformedJson)

        assertFalse(success)
    }
}
