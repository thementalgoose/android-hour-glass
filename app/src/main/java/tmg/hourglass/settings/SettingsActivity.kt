package tmg.hourglass.settings

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import dagger.hilt.android.AndroidEntryPoint
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.aboutthisapp.AboutThisAppConfiguration
import tmg.aboutthisapp.AboutThisAppDependency
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.extensions.updateAllWidgets
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.ThemePref
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.settings.layout.*
import tmg.hourglass.settings.privacy.PrivacyPolicyActivity
import tmg.hourglass.settings.release.ReleaseActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity: BaseActivity() {

    private val viewModel: SettingsViewModel by viewModels()

    @Inject
    protected lateinit var prefManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()


        observe(viewModel.outputs.currentThemePref) {
            when (it) {
                ThemePref.AUTO -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                ThemePref.LIGHT -> setDefaultNightMode(MODE_NIGHT_NO)
                ThemePref.DARK -> setDefaultNightMode(MODE_NIGHT_YES)
            }
        }

        observeEvent(viewModel.outputs.openAbout) {
            startActivity(AboutThisAppActivity.intent(this, aboutThisAppConfiguration))
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }

        observeEvent(viewModel.outputs.openReleaseNotes) {
            startActivity(ReleaseActivity.intent(this))
        }

        observeEvent(viewModel.outputs.updateWidget) {
            this.updateAllWidgets()
        }

        observeEvent(viewModel.outputs.openSuggestions) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            intent.putExtra(Intent.EXTRA_EMAIL, "thementalgoose@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
        }

        observeEvent(viewModel.outputs.openReview) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(applicationContext, getString(R.string.error_activity_not_found), Toast.LENGTH_LONG).show()
            }
        }

        observeEvent(viewModel.outputs.privacyPolicy) {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    private fun setContent() {
        setContent {
            AppTheme {
                Scaffold(
                    content = {
                        val openDeleteAllDialog = remember { mutableStateOf(false) }
                        val openThemeDialog = remember { mutableStateOf(false) }

                        val theme = viewModel.outputs.currentThemePref.observeAsState(ThemePref.AUTO)
                        val settingsItems = viewModel.outputs.list.observeAsState(emptyList())

                        SettingsLayout(
                            list = settingsItems.value,
                            modelClicked = {
                                when (it.id) {
                                    R.string.settings_theme_theme_title -> {
                                        openThemeDialog.value = true
                                    }
                                    R.string.settings_reset_all_title -> {
                                        openDeleteAllDialog.value = true
                                    }
                                    else -> {
                                        viewModel.inputs.clickSetting(it)
                                    }
                                }
                            },
                            clickBack = viewModel.inputs::clickBack
                        )

                        if (openDeleteAllDialog.value) {
                            DeleteDialog(
                                confirmed = {
                                    viewModel.inputs.clickDeleteAll()
                                    openDeleteAllDialog.value = false
                                    Toast.makeText(applicationContext, R.string.settings_all_deleted, Toast.LENGTH_LONG).show()
                                },
                                dismissed = {
                                    openDeleteAllDialog.value = false
                                }
                            )
                        }
                        if (openThemeDialog.value) {
                            ThemeDialog(
                                theme = theme.value,
                                themePicked = viewModel.inputs::clickTheme,
                                dismissed = {
                                    openThemeDialog.value = false
                                }
                            )
                        }
                    }
                )
            }
        }
    }



    private val aboutThisAppConfiguration
        get() = AboutThisAppConfiguration(
            themeRes = R.style.AppTheme_AboutThisApp,
            name = getString(R.string.about_name),
            nameDesc = getString(R.string.about_desc),
            imageRes = R.mipmap.ic_launcher,
            subtitle = getString(R.string.dependency_thank_you),
            footnote = "", // getString(R.string.about_additional),
            appVersion = BuildConfig.VERSION_NAME,
            appPackageName = "tmg.hourglass",
            appName = getString(R.string.app_name),
            play = "https://play.google.com/store/apps/details?id=tmg.hourglass",
            email = "thementalgoose@gmail.com",
            dependencies = projectDependencies(),
            guid = prefManager.deviceUdid,
            guidLongClickCopy = true
        )

    private fun projectDependencies(): List<AboutThisAppDependency> = listOf(
        AboutThisAppDependency(
            order = 0,
            dependencyName = "Jetpack",
            author = "Google",
            url = "https://developer.android.com/jetpack",
            backgroundColor = Color.WHITE,
            imageUrl = "https://avatars.githubusercontent.com/u/6955922?s=200&v=4"
        ),
        AboutThisAppDependency(
            order = 1,
            dependencyName = "Firebase",
            author = "Google",
            url = "https://firebase.google.com/",
            backgroundColor = Color.TRANSPARENT,
            imageUrl = "https://avatars2.githubusercontent.com/u/1335026"
        ),
        AboutThisAppDependency(
            order = 2,
            dependencyName = "Realm",
            author = "Realm",
            url = "https://realm.io",
            imageUrl = "https://avatars1.githubusercontent.com/u/7575099"
        ),
        AboutThisAppDependency(
            order = 3,
            dependencyName = "Shaky",
            author = "Linked In",
            url = "https://github.com/linkedin/shaky-android",
            imageUrl = "https://avatars.githubusercontent.com/u/357098"
        ),
        AboutThisAppDependency(
            order = 4,
            dependencyName = "ThreeTen",
            author = "Jake Wharton",
            url = "https://github.com/JakeWharton/ThreeTenABP",
            imageUrl = "https://avatars0.githubusercontent.com/u/66577"
        ),
        AboutThisAppDependency(
            order = 5,
            dependencyName = "ColorSheet",
            author = "Kristiyan Petrov",
            url = "https://github.com/kristiyanP/colorpicker",
            imageUrl = "https://avatars3.githubusercontent.com/u/5365351"
        )
    )

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}