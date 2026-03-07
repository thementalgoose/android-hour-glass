package tmg.hourglass.domain.model

data class Tag(
    val tagId: String,
    val name: String,
    val colour: String,
    val sort: TagOrdering
) {
    companion object
}

fun Tag.Companion.preview(
    tagId: String = "tagId",
    name: String = "Tag",
    colour: String = "#888888",
    sort: TagOrdering = TagOrdering.ALPHABETICAL
): Tag = Tag(
    tagId = tagId,
    name = name,
    colour = colour,
    sort = sort
)