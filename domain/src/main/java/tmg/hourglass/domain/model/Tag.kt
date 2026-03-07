package tmg.hourglass.domain.model

data class Tag(
    val tagId: String,
    val name: String,
    val colour: String,
    val sort: TagOrdering
)