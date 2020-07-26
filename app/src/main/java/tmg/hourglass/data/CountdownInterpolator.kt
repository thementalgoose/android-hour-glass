package tmg.hourglass.data

enum class CountdownInterpolator(
    val key: String
) {
    LINEAR("linear"),
    ACCELERATE("accelerate"),
    DECELERATE("decelerate"),
    ACCELERATE_DECELERATE("accelerateDecelerate")
}