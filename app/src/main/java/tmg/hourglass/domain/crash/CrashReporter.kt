package tmg.hourglass.domain.crash

interface CrashReporter {
    fun log(msg: String)
    fun logException(throwable: Throwable)
}