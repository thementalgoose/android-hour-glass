package tmg.hourglass.aboutthisapp

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.graphics.toArgb
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.aboutthisapp.configuration.Colours
import tmg.aboutthisapp.configuration.Configuration
import tmg.aboutthisapp.configuration.Dependency
import tmg.aboutthisapp.configuration.DependencyIcon
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.darkColors
import tmg.hourglass.presentation.dynamic
import tmg.hourglass.presentation.lightColors
import tmg.utilities.extensions.isInNightMode
import javax.inject.Inject

class AboutThisAppConfig @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val prefManager: PreferencesManager,
) {

    fun startActivity(appCompatActivity: ComponentActivity) {
        appCompatActivity.startActivity(AboutThisAppActivity.intent(appCompatActivity, aboutThisAppConfiguration))
    }

    private val aboutThisAppConfiguration
        get() = Configuration(
            imageRes = R.mipmap.ic_launcher,
            appName = context.getString(R.string.app_name),
            appVersion = BuildConfig.VERSION_NAME,
            appPackageName = "tmg.hourglass",
            dependencies = projectDependencies(),
            header = context.getString(tmg.hourglass.strings.R.string.dependency_thank_you),
            email = "thementalgoose@gmail.com",
            github = "https://www.github.com/thementalgoose/android-hour-glass",
            debugInfo = prefManager.deviceUdid,
            lightColors = getColours(isLight = true),
            darkColors = getColours(isLight = false),
            setIsDarkMode = when (prefManager.theme) {
                ThemePref.AUTO -> context.isInNightMode()
                ThemePref.LIGHT -> false
                ThemePref.DARK -> true
            }
        )

    //region Colours

    private fun getColours(isLight: Boolean): Colours {
        val colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (isLight) {
                lightColors.dynamic(dynamicLightColorScheme(context), isLightMode = true)
            } else {
                darkColors.dynamic(dynamicDarkColorScheme(context), isLightMode = false)
            }
        } else {
            if (isLight) lightColors else darkColors
        }

        return Colours(
            colorPrimary = colors.primary.toArgb(),
            background = colors.backgroundPrimary.toArgb(),
            surface = colors.backgroundSecondary.toArgb(),
            primary = colors.backgroundTertiary.toArgb(),
            onBackground = colors.textPrimary.toArgb(),
            onSurface = colors.textSecondary.toArgb(),
            onPrimary = colors.textSecondary.toArgb(),
        )
    }

    //endregion

    private fun projectDependencies(): List<Dependency> = listOf(
        Dependency(
            dependencyName = "Jetpack",
            author = "Google",
            url = "https://developer.android.com/jetpack",
            icon = DependencyIcon.Image(
                url = "https://avatars.githubusercontent.com/u/6955922?s=200&v=4",
                backgroundColor = Color.WHITE
            )
        ),
        Dependency(
            dependencyName = "Firebase",
            author = "Google",
            url = "https://firebase.google.com/",
            icon = DependencyIcon.Image(
                url = "https://avatars2.githubusercontent.com/u/1335026",
                backgroundColor = Color.TRANSPARENT
            )
        ),
        Dependency(
            dependencyName = "Realm",
            author = "Realm",
            url = "https://realm.io",
            icon = DependencyIcon.Image(
                url = "https://avatars1.githubusercontent.com/u/7575099"
            )
        ),
        Dependency(
            dependencyName = "Shaky",
            author = "Linked In",
            url = "https://github.com/linkedin/shaky-android",
            icon = DependencyIcon.Image(
                url = "https://avatars.githubusercontent.com/u/357098"
            )
        ),
        Dependency(
            dependencyName = "ColorSheet",
            author = "Kristiyan Petrov",
            url = "https://github.com/kristiyanP/colorpicker",
            icon = DependencyIcon.Image(
                url = "https://avatars3.githubusercontent.com/u/5365351"
            )
        )
    )
}