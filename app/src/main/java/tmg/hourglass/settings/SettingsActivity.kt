package tmg.hourglass.settings

import android.app.AlertDialog
import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_settings.*
import org.koin.android.ext.android.inject
import tmg.components.about.AboutThisAppActivity
import tmg.components.about.AboutThisAppDependency
import tmg.hourglass.BuildConfig
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.extensions.setOnClickListener
import tmg.hourglass.settings.release.ReleaseActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import java.lang.RuntimeException

class SettingsActivity: BaseActivity() {

    private val viewModel: SettingsViewModel by inject()

    override fun layoutId(): Int = R.layout.activity_settings

    override fun initViews() {
        ibtnClose.setOnClickListener(viewModel.inputs::clickBack)

        llResetAll.setOnClickListener {
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
        llResetDone.setOnClickListener {
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

        llHelpAbout.setOnClickListener(viewModel.inputs::clickAbout)
        llHelpReleaseNotes.setOnClickListener(viewModel.inputs::clickReleaseNotes)
        llHelpCrashReporting.setOnClickListener(viewModel.inputs::clickCrashReporting)
        llHelpSuggestions.setOnClickListener(viewModel.inputs::clickSuggestions)
        llHelpShakeToReport.setOnClickListener(viewModel.inputs::clickShakeToReport)

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }

        observeEvent(viewModel.outputs.deletedAll) {
            Snackbar.make(llResetAll, getString(R.string.settings_reset_all_done), Snackbar.LENGTH_LONG).show()
            finish()
        }

        observeEvent(viewModel.outputs.deletedDone) {
            Snackbar.make(llResetAll, getString(R.string.settings_reset_done_done), Snackbar.LENGTH_LONG).show()
            finish()
        }

        observeEvent(viewModel.outputs.openAbout) {
            openAbout()
        }

        observeEvent(viewModel.outputs.openReleaseNotes) {
            startActivity(Intent(this, ReleaseActivity::class.java))
        }

        observe(viewModel.outputs.crashReporting) { (showUpdate, value) ->
            imgCrashReporting.setImageResource(if (value) R.drawable.ic_settings_check else 0)
            imgCrashReporting.setBackgroundResource(if (value) R.drawable.background_selected else R.drawable.background_edit_text)
            if (showUpdate) {
                Snackbar.make(
                    llResetAll,
                    getString(R.string.settings_help_crash_reporting_after_app_restart),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        observe(viewModel.outputs.openSuggestions) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            intent.putExtra(Intent.EXTRA_EMAIL, "thementalgoose@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
        }

        observe(viewModel.outputs.shakeToReport) { (showUpdate, value) ->
            imgShakeToReport.setImageResource(if (value) R.drawable.ic_settings_check else 0)
            imgShakeToReport.setBackgroundResource(if (value) R.drawable.background_selected else R.drawable.background_edit_text)
            if (showUpdate) {
                Snackbar.make(
                    llResetAll,
                    getString(R.string.settings_help_shake_to_report_after_app_restart),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun openAbout() {
        startActivity(
            AboutThisAppActivity.intent(
                this,
                isDarkMode = false,
                name = getString(R.string.about_name),
                nameDesc = getString(R.string.about_desc),
                imageUrl = "https://avatars1.githubusercontent.com/u/5982159",
                thankYou = getString(R.string.dependency_thank_you),
                footnote = "", // getString(R.string.about_additional),
                appVersion = BuildConfig.VERSION_NAME,
                appName = getString(R.string.app_name),
                website = "https://jordanfisher.io",
                email = "thementalgoose@gmail.com",
                packageName = packageName ?: "",
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
                        dependencyName = "TMG Utilities + Components",
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
}