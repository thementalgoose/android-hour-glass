package tmg.hourglass

import tmg.utilities.extensions.toEnum

enum class Env(
    val id: Int
) {
    SAND(1),
    LIVE(0);

    companion object {
        fun getById(id: Int) = id.toEnum<Env> { it.id } ?: LIVE
    }
}