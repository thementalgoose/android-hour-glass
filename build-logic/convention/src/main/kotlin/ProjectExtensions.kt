import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * Extension property to access the version catalog in convention plugins
 */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Extension function to configure Android common settings
 */
@Suppress("UnstableApiUsage")
internal fun Project.android(configure: CommonExtension.() -> Unit) {
    extensions.configure("android", configure)
}
