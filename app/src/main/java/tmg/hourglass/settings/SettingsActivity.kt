package tmg.hourglass.settings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.bottom_sheet_theme.*
import org.koin.android.ext.android.inject
import tmg.components.about.AboutThisAppActivity
import tmg.components.about.AboutThisAppDependency
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.extensions.setOnClickListener
import tmg.hourglass.extensions.updateAllWidgets
import tmg.hourglass.extensions.updateWidgets
import tmg.hourglass.prefs.ThemePref
import tmg.hourglass.settings.privacy.PrivacyPolicyActivity
import tmg.hourglass.settings.release.ReleaseActivity
import tmg.hourglass.widget.dark.ItemWidgetDarkProvider
import tmg.hourglass.widget.light.ItemWidgetLightProvider
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.*


class SettingsActivity : BaseActivity() {

    private val viewModel: SettingsViewModel by inject()
    private lateinit var themeBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var adapter: SettingsAdapter

    override fun layoutId(): Int = R.layout.activity_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeBottomSheet = BottomSheetBehavior.from(bsTheme)
        themeBottomSheet.isHideable = true
        themeBottomSheet.addBottomSheetCallback(BottomSheetFader(vBackground, "theme"))
        themeBottomSheet.hidden()
        vBackground.setOnClickListener { themeBottomSheet.hidden() }

        adapter = SettingsAdapter(
            prefClicked = { prefClicked(it) },
            prefSwitchClicked = { key, value -> prefSwitched(key, value) }
        )
        rvSettings.adapter = adapter
        rvSettings.layoutManager = LinearLayoutManager(this)

        ibtnClose.setOnClickListener(viewModel.inputs::clickBack)

        clThemeAuto.setOnClickListener { viewModel.inputs.clickTheme(ThemePref.AUTO) }
        clThemeLight.setOnClickListener { viewModel.inputs.clickTheme(ThemePref.LIGHT) }
        clThemeDark.setOnClickListener { viewModel.inputs.clickTheme(ThemePref.DARK) }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }

        observeEvent(viewModel.outputs.deletedAll) {
            Snackbar.make(
                rvSettings,
                getString(R.string.settings_reset_all_done),
                Snackbar.LENGTH_LONG
            ).show()
            finish()
        }

        observeEvent(viewModel.outputs.deletedDone) {
            Snackbar.make(
                rvSettings,
                getString(R.string.settings_reset_done_done),
                Snackbar.LENGTH_LONG
            ).show()
            finish()
        }


        observe(viewModel.outputs.themeSelected) {
            updateThemePicker(it)
            themeBottomSheet.hidden()
        }

        observeEvent(viewModel.outputs.themeUpdated) {
            Toast.makeText(
                applicationContext,
                getString(R.string.settings_theme_applied_later),
                Toast.LENGTH_LONG
            ).show()
        }




        observeEvent(viewModel.outputs.openAbout) {
            openAbout()
        }

        observeEvent(viewModel.outputs.openReleaseNotes) {
            startActivity(Intent(this, ReleaseActivity::class.java))
        }
//
        observe(viewModel.outputs.crashReporting) { (showUpdate, value) ->
            if (showUpdate) {
                Snackbar.make(
                    rvSettings,
                    getString(R.string.settings_help_crash_reporting_after_app_restart),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        observeEvent(viewModel.outputs.openSuggestions) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            intent.putExtra(Intent.EXTRA_EMAIL, "thementalgoose@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
        }

        observe(viewModel.outputs.shakeToReport) { (showUpdate, value) ->
            if (showUpdate) {
                Snackbar.make(
                    rvSettings,
                    getString(R.string.settings_help_shake_to_report_after_app_restart),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        observeEvent(viewModel.outputs.privacyPolicy) {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }
    }

    private fun prefClicked(key: String) {
        when (key.toEnum<SettingsViewModel.PrefType> { it.key }) {
            SettingsViewModel.PrefType.THEME_APP -> {
                themeBottomSheet.expand()
            }
            SettingsViewModel.PrefType.DELETE_ALL -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.settings_reset_all_confirm_title)
                    .setMessage(R.string.settings_reset_all_confirm_description)
                    .setPositiveButton(R.string.settings_reset_all_confirm_confirm) { _, _ ->
                        viewModel.inputs.clickDeleteAll()
                    }
                    .setNegativeButton(R.string.settings_reset_all_confirm_cancel) { _, _ -> }
                    .setCancelable(false)
                    .create()
                    .show()
            }
            SettingsViewModel.PrefType.DELETE_DONE -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.settings_reset_done_confirm_title)
                    .setMessage(R.string.settings_reset_done_confirm_description)
                    .setPositiveButton(R.string.settings_reset_done_confirm_confirm) { _, _ ->
                        viewModel.inputs.clickDeleteDone()
                    }
                    .setNegativeButton(R.string.settings_reset_done_confirm_cancel) { _, _ -> }
                    .setCancelable(false)
                    .create()
                    .show()
            }
            SettingsViewModel.PrefType.WIDGETS_REFRESH -> {
                updateAllWidgets()
                Snackbar.make(rvSettings, getString(R.string.settings_widgets_refresh_refreshed), Snackbar.LENGTH_LONG).show()
            }
            SettingsViewModel.PrefType.HELP_ABOUT -> {
                viewModel.inputs.clickAbout()
            }
            SettingsViewModel.PrefType.HELP_RELEASE -> {
                viewModel.inputs.clickReleaseNotes()
            }
            SettingsViewModel.PrefType.FEEDBACK_SUGGESTION -> {
                viewModel.inputs.clickSuggestions()
            }
            SettingsViewModel.PrefType.PRIVACY_PRIVACY -> {
                viewModel.inputs.clickPrivacyPolicy()
            }
            else -> throw Error("Regular pref type $key not supported")
        }
    }

    private fun prefSwitched(key: String, toNewValue: Boolean) {
        when (key.toEnum<SettingsViewModel.PrefType> { it.key }) {
            SettingsViewModel.PrefType.FEEDBACK_CRASH_REPORTING -> {
                viewModel.inputs.clickCrashReporting(toNewValue)
            }
            SettingsViewModel.PrefType.WIDGETS_UPDATED -> {
                viewModel.inputs.clickWidgetUpdate(toNewValue)
            }
            SettingsViewModel.PrefType.FEEDBACK_SHAKE -> {
                viewModel.inputs.clickShakeToReport(toNewValue)
            }
            else -> throw Error("Regular pref type $key not supported")
        }
    }

    private fun openAbout() {
        startActivity(
            AboutThisAppActivity.intent(
                this,
                isDarkMode = when (prefs.theme) {
                    ThemePref.AUTO -> this.isInNightMode()
                    ThemePref.LIGHT -> false
                    ThemePref.DARK -> true
                },
                name = getString(R.string.about_name),
                nameDesc = getString(R.string.about_desc),
                imageUrl = "https://lh3.googleusercontent.com/DcgSFsCSPOmXW8fEP32cZ44B1KqX_gk-8prh7Qd7nXhnugYE6Nhl1FE_aKaF9q62B6w",
                thankYou = getString(R.string.dependency_thank_you),
                footnote = "", // getString(R.string.about_additional),
                appVersion = BuildConfig.VERSION_NAME,
                appName = getString(R.string.app_name),
                play = "https://play.google.com/store/apps/details?id=tmg.hourglass",
                email = "thementalgoose@gmail.com",
                dependencies = listOf(
                    AboutThisAppDependency(
                        order = 1,
                        dependencyName = "Realm",
                        author = "Realm",
                        url = "https://realm.io",
                        imageUrl = "https://avatars1.githubusercontent.com/u/7575099"
                    ),
                    AboutThisAppDependency(
                        order = 2,
                        dependencyName = "Koin",
                        author = "Koin",
                        url = "https://github.com/InsertKoinIO/koin",
                        imageUrl = "https://avatars1.githubusercontent.com/u/38280958"
                    ),
                    AboutThisAppDependency(
                        order = 3,
                        dependencyName = "BugShaker Android",
                        author = "Stuart Kent",
                        url = "https://github.com/stkent/bugshaker-android",
                        imageUrl = "https://avatars0.githubusercontent.com/u/6463980"
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
                    ),
                    AboutThisAppDependency(
                        order = 6,
                        dependencyName = "Utilities + Components",
                        author = "Jordan Fisher",
                        url = "https://github.com/thementalgoose/android-components",
                        imageUrl = "https://avatars2.githubusercontent.com/u/5982159"
                    ),
                    AboutThisAppDependency(
                        order = 7,
                        dependencyName = "SmoothDateRangePicker",
                        author = "Leav Jean",
                        url = "https://github.com/leavjenn/SmoothDateRangePicker",
                        imageUrl = "https://avatars2.githubusercontent.com/u/13175621"
                    )
                )
            )
        )
    }

    private fun updateThemePicker(theme: ThemePref) {
        imgAuto.setImageResource(if (theme == ThemePref.AUTO) R.drawable.ic_settings_check else 0)
        imgAuto.setBackgroundResource(if (theme == ThemePref.AUTO) R.drawable.background_selected else R.drawable.background_edit_text)
        imgLight.setImageResource(if (theme == ThemePref.LIGHT) R.drawable.ic_settings_check else 0)
        imgLight.setBackgroundResource(if (theme == ThemePref.LIGHT) R.drawable.background_selected else R.drawable.background_edit_text)
        imgDark.setImageResource(if (theme == ThemePref.DARK) R.drawable.ic_settings_check else 0)
        imgDark.setBackgroundResource(if (theme == ThemePref.DARK) R.drawable.background_selected else R.drawable.background_edit_text)
    }
}