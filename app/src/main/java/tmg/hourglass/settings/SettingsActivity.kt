package tmg.hourglass.settings

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import tmg.components.about.AboutThisAppActivity
import tmg.components.about.AboutThisAppConfiguration
import tmg.components.about.AboutThisAppDependency
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.databinding.ActivitySettingsBinding
import tmg.hourglass.extensions.setOnClickListener
import tmg.hourglass.extensions.updateAllWidgets
import tmg.hourglass.prefs.ThemePref
import tmg.hourglass.settings.privacy.PrivacyPolicyActivity
import tmg.hourglass.settings.release.ReleaseActivity
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.*

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsViewModel by inject()
    private lateinit var themeBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var adapter: SettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        themeBottomSheet = BottomSheetBehavior.from(binding.bottomSheetTheme.bsTheme)
        themeBottomSheet.isHideable = true
        themeBottomSheet.addBottomSheetCallback(BottomSheetFader(binding.vBackground, "theme"))
        themeBottomSheet.hidden()
        binding.vBackground.setOnClickListener { themeBottomSheet.hidden() }

        adapter = SettingsAdapter(
            prefClicked = { prefClicked(it) },
            prefSwitchClicked = { key, value -> prefSwitched(key, value) }
        )
        binding.rvSettings.adapter = adapter
        binding.rvSettings.layoutManager = LinearLayoutManager(this)

        binding.ibtnClose.setOnClickListener(viewModel.inputs::clickBack)

        binding.bottomSheetTheme.clThemeAuto.setOnClickListener { viewModel.inputs.clickTheme(ThemePref.AUTO) }
        binding.bottomSheetTheme.clThemeLight.setOnClickListener { viewModel.inputs.clickTheme(ThemePref.LIGHT) }
        binding.bottomSheetTheme.clThemeDark.setOnClickListener { viewModel.inputs.clickTheme(ThemePref.DARK) }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }

        observeEvent(viewModel.outputs.deletedAll) {
            Snackbar.make(
                binding.rvSettings,
                getString(R.string.settings_reset_all_done),
                Snackbar.LENGTH_LONG
            ).show()
            finish()
        }

        observeEvent(viewModel.outputs.deletedDone) {
            Snackbar.make(
                binding.rvSettings,
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

        observeEvent(viewModel.outputs.openReview) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) { }
        }

        observeEvent(viewModel.outputs.openReleaseNotes) {
            startActivity(Intent(this, ReleaseActivity::class.java))
        }

        observe(viewModel.outputs.crashReporting) { (showUpdate, _) ->
            if (showUpdate) {
                Snackbar.make(
                    binding.rvSettings,
                    getString(R.string.settings_help_crash_reporting_after_app_restart),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        observe(viewModel.outputs.analyticsReporting) { (showUpdate, _) ->
            if (showUpdate) {
                Snackbar.make(
                    binding.rvSettings,
                    getString(R.string.settings_help_analytics_after_app_restart),
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

        observe(viewModel.outputs.shakeToReport) { (showUpdate, _) ->
            if (showUpdate) {
                Snackbar.make(
                    binding.rvSettings,
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
                Snackbar.make(
                    binding.rvSettings,
                    getString(R.string.settings_widgets_refresh_refreshed),
                    Snackbar.LENGTH_LONG
                ).show()
            }
            SettingsViewModel.PrefType.HELP_ABOUT -> {
                viewModel.inputs.clickAbout()
            }
            SettingsViewModel.PrefType.HELP_REVIEW -> {
                viewModel.inputs.clickReview()
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
            SettingsViewModel.PrefType.FEEDBACK_ANALYTICS -> {
                viewModel.inputs.clickAnalytics(toNewValue)
            }
            else -> throw Error("Regular pref type $key not supported")
        }
    }

    private fun openAbout() {
        startActivity(AboutThisAppActivity.intent(this,aboutThisAppConfiguration))
    }

    private fun updateThemePicker(theme: ThemePref) {
        binding.bottomSheetTheme.imgAuto.setImageResource(if (theme == ThemePref.AUTO) R.drawable.ic_settings_check else 0)
        binding.bottomSheetTheme.imgAuto.setBackgroundResource(if (theme == ThemePref.AUTO) R.drawable.background_selected else R.drawable.background_edit_text)
        binding.bottomSheetTheme.imgLight.setImageResource(if (theme == ThemePref.LIGHT) R.drawable.ic_settings_check else 0)
        binding.bottomSheetTheme.imgLight.setBackgroundResource(if (theme == ThemePref.LIGHT) R.drawable.background_selected else R.drawable.background_edit_text)
        binding.bottomSheetTheme.imgDark.setImageResource(if (theme == ThemePref.DARK) R.drawable.ic_settings_check else 0)
        binding.bottomSheetTheme.imgDark.setBackgroundResource(if (theme == ThemePref.DARK) R.drawable.background_selected else R.drawable.background_edit_text)
    }

    private fun getAboutTheme(): Int {
        return when (prefs.theme) {
            ThemePref.AUTO -> if (isInNightMode()) R.style.DarkTheme_AboutThisApp else R.style.LightTheme_AboutThisApp
            ThemePref.LIGHT -> R.style.LightTheme_AboutThisApp
            ThemePref.DARK -> R.style.DarkTheme_AboutThisApp
        }
    }

    private val aboutThisAppConfiguration
        get() = AboutThisAppConfiguration(
            themeRes = getAboutTheme(),
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
            dependencies = projectDependencies()
        )

    private fun projectDependencies(): List<AboutThisAppDependency> = listOf(
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
        )
    )
}