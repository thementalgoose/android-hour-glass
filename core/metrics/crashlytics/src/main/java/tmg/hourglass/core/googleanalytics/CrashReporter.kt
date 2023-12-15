package tmg.hourglass.core.googleanalytics

interface CrashReporter {
    fun log(msg: String)
    fun logException(throwable: Throwable)
}