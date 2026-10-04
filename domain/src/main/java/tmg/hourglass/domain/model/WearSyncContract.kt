package tmg.hourglass.domain.model

object WearSyncContract {
    const val CURRENT_SCHEMA_VERSION = 1
    const val COUNTDOWNS_PATH = "/countdowns"
    const val KEY_COUNTDOWNS_JSON = "countdowns_json"
    const val KEY_TIMESTAMP = "timestamp"
    const val KEY_SCHEMA_VERSION = "schema_version"

    fun isSchemaVersionSupported(version: Int): Boolean {
        return version <= CURRENT_SCHEMA_VERSION
    }
}
