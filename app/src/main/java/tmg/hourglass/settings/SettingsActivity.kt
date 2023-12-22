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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import dagger.hilt.android.AndroidEntryPoint
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.aboutthisapp.ConfigurationColours
import tmg.aboutthisapp.configuration.Configuration
import tmg.aboutthisapp.configuration.Dependency
import tmg.aboutthisapp.configuration.DependencyIcon
import tmg.hourglass.presentation.lightColors
import tmg.hourglass.presentation.darkColors
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.strings.R.string
import tmg.hourglass.extensions.updateAllWidgets
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.presentation.ThemePref
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.settings.components.DeleteDialog
import tmg.hourglass.presentation.settings.components.ThemeDialog
import tmg.hourglass.settings.layout.*
import tmg.hourglass.settings.privacy.PrivacyPolicyActivity
import tmg.hourglass.settings.release.ReleaseActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity: AppCompatActivity() {

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
            startActivity(Intent.createChooser(intent, getString(string.send_email)))
        }

        observeEvent(viewModel.outputs.openReview) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(applicationContext, getString(string.error_activity_not_found), Toast.LENGTH_LONG).show()
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
                                    string.settings_theme_theme_title -> {
                                        openThemeDialog.value = true
                                    }
                                    string.settings_reset_all_title -> {
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
                                    Toast.makeText(applicationContext, string.settings_all_deleted, Toast.LENGTH_LONG).show()
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

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}