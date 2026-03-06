package tmg.hourglass.domain.model

sealed interface TaggedCountdowns {
    val countdowns: List<Countdown>
    val id: String

    data class Tagged(
        val tag: Tag,
        override val countdowns: List<Countdown>
    ): TaggedCountdowns {
        override val id: String
            get() = tag.tagId
    }

    data class Untagged(
        override val countdowns: List<Countdown>
    ): TaggedCountdowns {
        override val id: String
            get() = "untagged"
    }
}