package tmg.hourglass.domain.model

sealed interface TaggedCountdowns {
    val countdowns: List<Countdown>
    val id: String
    val sort: TagOrdering

    data class Tagged(
        val tag: Tag,
        override val countdowns: List<Countdown>
    ): TaggedCountdowns {
        override val id: String
            get() = tag.tagId

        override val sort: TagOrdering
            get() = tag.sort
    }

    data class Untagged(
        override val sort: TagOrdering,
        override val countdowns: List<Countdown>
    ): TaggedCountdowns {
        override val id: String
            get() = "untagged"

    }
}