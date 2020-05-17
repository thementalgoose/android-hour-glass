package tmg.hourglass.crash

interface CrashReporter {
    fun log(msg: String)
    fun logException(throwable: Throwable)
}