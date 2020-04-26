package tmg.hourglass.extensions

val Int.hexColour: String
    get() = String.format("#%06X", 0xFFFFFF and this)