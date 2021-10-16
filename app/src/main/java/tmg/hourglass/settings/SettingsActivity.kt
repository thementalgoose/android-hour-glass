package tmg.hourglass.settings

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import tmg.aboutthisapp.AboutThisAppActivity
import tmg.aboutthisapp.AboutThisAppConfiguration
import tmg.aboutthisapp.AboutThisAppDependency
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.databinding.ActivitySettingsBinding
import tmg.hourglass.extensions.setOnClickListener
import tmg.hourglass.extensions.updateAllWidgets
import tmg.hourglass.prefs.PreferencesManager
import tmg.hourglass.prefs.ThemePref
import tmg.hourglass.settings.privacy.PrivacyPolicyActivity
import tmg.hourglass.settings.release.ReleaseActivity
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.*

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsViewModel by inject()

    private val prefManager: PreferencesManager by inject()

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
            clickPref = viewModel.inputs::clickSetting
        )
        binding.rvSettings.adapter = adapter
        binding.rvSettings.layoutManager = LinearLayoutManager(this)

        binding.ibtnClose.setOnClickListener(viewModel.inputs::clickBack)

        binding.bottomSheetTheme.clThemeAuto.setOnClickListener {
            viewModel.inputs.clickTheme(
                ThemePref.AUTO
            )
        }
        binding.bottomSheetTheme.clThemeLight.setOnClickListener {
            viewModel.inputs.clickTheme(
                ThemePref.LIGHT
            )
        }
        binding.bottomSheetTheme.clThemeDark.setOnClickListener {
            viewModel.inputs.clickTheme(
                ThemePref.DARK
            )
        }

        observe(viewModel.outputs.updateWidget) {
            updateAllWidgets()
            Snackbar
                .make(binding.rvSettings, getString(R.string.settings_widgets_refresh_refreshed), Snackbar.LENGTH_LONG)
                .show()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }

        observeEvent(viewModel.outputs.deletedAll) {
            AlertDialog.Builder(this)
                .setTitle(R.string.settings_reset_all_confirm_title)
                .setMessage(R.string.settings_reset_all_confirm_description)
                .setPositiveButton(R.string.settings_reset_all_confirm_confirm) { _, _ ->
                    viewModel.inputs.clickDeleteAll()
                    Snackbar.make(binding.rvSettings, getString(R.string.settings_reset_all_done), Snackbar.LENGTH_LONG)
                        .show()
                    finish()
                }
                .setNegativeButton(R.string.settings_reset_all_confirm_cancel) { _, _ -> }
                .setCancelable(false)
                .create()
                .show()
        }

        observeEvent(viewModel.outputs.deletedDone) {
            AlertDialog.Builder(this)
                .setTitle(R.string.settings_reset_done_confirm_title)
                .setMessage(R.string.settings_reset_done_confirm_description)
                .setPositiveButton(R.string.settings_reset_done_confirm_confirm) { _, _ ->
                    viewModel.inputs.clickDeleteDone()
                    Snackbar.make(binding.rvSettings, getString(R.string.settings_reset_done_done), Snackbar.LENGTH_LONG)
                        .show()
                    finish()
                }
                .setNegativeButton(R.string.settings_reset_done_confirm_cancel) { _, _ -> }
                .setCancelable(false)
                .create()
                .show()
        }


        observeEvent(viewModel.outputs.openTheme) {
            themeBottomSheet.expand()
        }

        observe(viewModel.outputs.currentThemePref) {
            updateThemePicker(it)
            when (it) {
                ThemePref.AUTO -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                ThemePref.LIGHT -> setDefaultNightMode(MODE_NIGHT_NO)
                ThemePref.DARK -> setDefaultNightMode(MODE_NIGHT_YES)
            }
        }

        observeEvent(viewModel.outputs.openAbout) {
            startActivity(AboutThisAppActivity.intent(this, aboutThisAppConfiguration))
        }

        observeEvent(viewModel.outputs.openReview) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
            }
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

    private fun updateThemePicker(theme: ThemePref) {
        binding.bottomSheetTheme.imgAuto.setImageResource(if (theme == ThemePref.AUTO) R.drawable.ic_settings_check else 0)
        binding.bottomSheetTheme.imgAuto.setBackgroundResource(if (theme == ThemePref.AUTO) R.drawable.background_selected else R.drawable.background_edit_text)
        binding.bottomSheetTheme.imgLight.setImageResource(if (theme == ThemePref.LIGHT) R.drawable.ic_settings_check else 0)
        binding.bottomSheetTheme.imgLight.setBackgroundResource(if (theme == ThemePref.LIGHT) R.drawable.background_selected else R.drawable.background_edit_text)
        binding.bottomSheetTheme.imgDark.setImageResource(if (theme == ThemePref.DARK) R.drawable.ic_settings_check else 0)
        binding.bottomSheetTheme.imgDark.setBackgroundResource(if (theme == ThemePref.DARK) R.drawable.background_selected else R.drawable.background_edit_text)
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