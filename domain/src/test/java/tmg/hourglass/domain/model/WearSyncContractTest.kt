package tmg.hourglass.domain.model

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WearSyncContractTest {

    @Test
    fun `isSchemaVersionSupported returns true for version equal or lower than current`() {
        assertTrue(WearSyncContract.isSchemaVersionSupported(1))
        assertTrue(WearSyncContract.isSchemaVersionSupported(0))
    }

    @Test
    fun `isSchemaVersionSupported returns false for version higher than current`() {
        assertFalse(WearSyncContract.isSchemaVersionSupported(2))
        assertFalse(WearSyncContract.isSchemaVersionSupported(99))
    }
}
