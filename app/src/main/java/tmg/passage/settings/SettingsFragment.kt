package tmg.passage.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import tmg.components.about.AboutThisAppActivity
import tmg.components.about.AboutThisAppDependency
import tmg.passage.BuildConfig
import tmg.passage.R

private const val keyPreferenceHelpAbout: String = "prefs_help_about"

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        about()
    }

    private fun about() {
        prefOnClick<Preference>(keyPreferenceHelpAbout) { pref ->
            startActivity(
                AboutThisAppActivity.intent(
                    requireContext(),
                    isDarkMode = false,
                    name = requireContext().getString(R.string.about_name),
                    nameDesc = requireContext().getString(R.string.about_desc),
                    imageUrl = "https://avatars1.githubusercontent.com/u/5982159",
                    thankYou = requireContext().getString(R.string.dependency_thank_you),
                    footnote = requireContext().getString(R.string.about_additional),
                    appVersion = BuildConfig.VERSION_NAME,
                    appName = requireContext().getString(R.string.app_name),
                    website = "https://jordanfisher.io",
                    email = "thementalgoose@gmail.com",
                    packageName = context?.packageName ?: "",
                    dependencies = listOf(
                        AboutThisAppDependency(
                            order = 0,
                            dependencyName = "Firebase",
                            author = "Google",
                            url = "https://firebase.google.com/",
                            imageUrl = "https://avatars2.githubusercontent.com/u/1335026"
                        ),
                        AboutThisAppDependency(
                            order = 5,
                            dependencyName = "Koin",
                            author = "Koin",
                            url = "https://github.com/InsertKoinIO/koin",
                            imageUrl = "https://avatars1.githubusercontent.com/u/38280958"
                        ),
                        AboutThisAppDependency(
                            order = 7,
                            dependencyName = "ColorSheet",
                            author = "Sasikanth",
                            url = "https://github.com/msasikanth/ColorSheet",
                            imageUrl = "https://avatars2.githubusercontent.com/u/6140516"
                        ),
                        AboutThisAppDependency(
                            order = 8,
                            dependencyName = "BugShaker Android",
                            author = "Stuart Kent",
                            url = "https://github.com/stkent/bugshaker-android",
                            imageUrl = "https://avatars0.githubusercontent.com/u/6463980"
                        ),
                        AboutThisAppDependency(
                            order = 9,
                            dependencyName = "ThreeTen",
                            author = "Jake Wharton",
                            url = "https://github.com/JakeWharton/ThreeTenABP",
                            imageUrl = "https://avatars0.githubusercontent.com/u/66577"
                        )
                    )
                )
            )
        }
    }

    private inline fun <reified T : Preference> prefOnClick(
        key: String,
        crossinline method: (pref: Preference) -> Unit
    ) {
        findPreference<T>(key)?.let {
            it.setOnPreferenceClickListener {
                method(it)
                return@setOnPreferenceClickListener true
            }
        }
    }

}