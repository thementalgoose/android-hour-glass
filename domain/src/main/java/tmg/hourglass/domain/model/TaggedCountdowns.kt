package tmg.hourglass.domain.model

sealed interface TaggedCountdowns {
    val countdowns: List<Countdown>

    data class Tagged(
        val tag: Tag,
        override val countdowns: List<Countdown>
    ): TaggedCountdowns

    data class Untagged(
        override val countdowns: List<Countdown>
    ): TaggedCountdowns
}