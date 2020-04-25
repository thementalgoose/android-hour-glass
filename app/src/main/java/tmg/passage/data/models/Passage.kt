package tmg.passage.data.models

import org.threeten.bp.LocalDateTime
import tmg.passage.data.PassageType

data class Passage(
    val id: String,
    val name: String,
    val description: String,
    val colour: String,

    val start: LocalDateTime,
    val end: LocalDateTime,
    val initial: String,
    val final: String,

    val passageType: PassageType
)