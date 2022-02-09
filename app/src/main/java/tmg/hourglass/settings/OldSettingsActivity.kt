package tmg.hourglass.settings

//class OldSettingsActivity : BaseActivity() {
//
//    private lateinit var binding: ActivitySettingsBinding
//
//    private val viewModel: SettingsViewModel by inject()
//
//    private val prefManager: PreferencesManager by inject()
//
//    private lateinit var themeBottomSheet: BottomSheetBehavior<LinearLayout>
//    private lateinit var adapter: SettingsAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySettingsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        themeBottomSheet = BottomSheetBehavior.from(binding.bottomSheetTheme.bsTheme)
//        themeBottomSheet.isHideable = true
//        themeBottomSheet.addBottomSheetCallback(BottomSheetFader(binding.vBackground, "theme"))
//        themeBottomSheet.hidden()
//        binding.vBackground.setOnClickListener { themeBottomSheet.hidden() }
//
//        adapter = SettingsAdapter(
//            clickPref = viewModel.inputs::clickSetting
//        )
//        binding.rvSettings.adapter = adapter
//        binding.rvSettings.layoutManager = LinearLayoutManager(this)
//
//        binding.ibtnClose.setOnClickListener(viewModel.inputs::clickBack)
//
//        binding.bottomSheetTheme.clThemeAuto.setOnClickListener {
//            viewModel.inputs.clickTheme(
//                ThemePref.AUTO
//            )
//        }
//        binding.bottomSheetTheme.clThemeLight.setOnClickListener {
//            viewModel.inputs.clickTheme(
//                ThemePref.LIGHT
//            )
//        }
//        binding.bottomSheetTheme.clThemeDark.setOnClickListener {
//            viewModel.inputs.clickTheme(
//                ThemePref.DARK
//            )
//        }
//
//        observe(viewModel.outputs.updateWidget) {
//            updateAllWidgets()
//            Snackbar
//                .make(binding.rvSettings, getString(R.string.settings_widgets_refresh_refreshed), Snackbar.LENGTH_LONG)
//                .show()
//        }
//
//        observe(viewModel.outputs.list) {
//            adapter.list = it
//        }
//
//        observeEvent(viewModel.outputs.goBack) {
//            finish()
//        }
//
//        observeEvent(viewModel.outputs.deletedAll) {
//            AlertDialog.Builder(this)
//                .setTitle(R.string.settings_reset_all_confirm_title)
//                .setMessage(R.string.settings_reset_all_confirm_description)
//                .setPositiveButton(R.string.settings_reset_all_confirm_confirm) { _, _ ->
//                    viewModel.inputs.clickDeleteAll()
//                    Snackbar.make(binding.rvSettings, getString(R.string.settings_reset_all_done), Snackbar.LENGTH_LONG)
//                        .show()
//                    finish()
//                }
//                .setNegativeButton(R.string.settings_reset_all_confirm_cancel) { _, _ -> }
//                .setCancelable(false)
//                .create()
//                .show()
//        }
//
//        observeEvent(viewModel.outputs.deletedDone) {
//            AlertDialog.Builder(this)
//                .setTitle(R.string.settings_reset_done_confirm_title)
//                .setMessage(R.string.settings_reset_done_confirm_description)
//                .setPositiveButton(R.string.settings_reset_done_confirm_confirm) { _, _ ->
//                    viewModel.inputs.clickDeleteDone()
//                    Snackbar.make(binding.rvSettings, getString(R.string.settings_reset_done_done), Snackbar.LENGTH_LONG)
//                        .show()
//                    finish()
//                }
//                .setNegativeButton(R.string.settings_reset_done_confirm_cancel) { _, _ -> }
//                .setCancelable(false)
//                .create()
//                .show()
//        }
//
//
//        observeEvent(viewModel.outputs.openTheme) {
//            themeBottomSheet.expand()
//        }
//
//        observe(viewModel.outputs.currentThemePref) {
//            updateThemePicker(it)
//            when (it) {
//                ThemePref.AUTO -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
//                ThemePref.LIGHT -> setDefaultNightMode(MODE_NIGHT_NO)
//                ThemePref.DARK -> setDefaultNightMode(MODE_NIGHT_YES)
//            }
//        }
//
//        observeEvent(viewModel.outputs.openAbout) {
//            startActivity(AboutThisAppActivity.intent(this, aboutThisAppConfiguration))
//        }
//
//        observeEvent(viewModel.outputs.openReview) {
//            try {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
//                startActivity(intent)
//            } catch (e: ActivityNotFoundException) {
//            }
//        }
//
//        observeEvent(viewModel.outputs.openReleaseNotes) {
//            startActivity(Intent(this, ReleaseActivity::class.java))
//        }
//
//        observe(viewModel.outputs.crashReporting) { (showUpdate, _) ->
//            if (showUpdate) {
//                Snackbar.make(
//                    binding.rvSettings,
//                    getString(R.string.settings_help_crash_reporting_after_app_restart),
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//        }
//
//        observe(viewModel.outputs.analyticsReporting) { (showUpdate, _) ->
//            if (showUpdate) {
//                Snackbar.make(
//                    binding.rvSettings,
//                    getString(R.string.settings_help_analytics_after_app_restart),
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//        }
//
//        observeEvent(viewModel.outputs.openSuggestions) {
//            val intent = Intent(Intent.ACTION_SEND)
//            intent.type = "text/html"
//            intent.putExtra(Intent.EXTRA_EMAIL, "thementalgoose@gmail.com")
//            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
//            startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
//        }
//
//        observe(viewModel.outputs.shakeToReport) { (showUpdate, _) ->
//            if (showUpdate) {
//                Snackbar.make(
//                    binding.rvSettings,
//                    getString(R.string.settings_help_shake_to_report_after_app_restart),
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//        }
//
//        observeEvent(viewModel.outputs.privacyPolicy) {
//            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
//        }
//    }
//
//    private fun updateThemePicker(theme: ThemePref) {
//        binding.bottomSheetTheme.imgAuto.setImageResource(if (theme == ThemePref.AUTO) R.drawable.ic_settings_check else 0)
//        binding.bottomSheetTheme.imgAuto.setBackgroundResource(if (theme == ThemePref.AUTO) R.drawable.background_selected else R.drawable.background_edit_text)
//        binding.bottomSheetTheme.imgLight.setImageResource(if (theme == ThemePref.LIGHT) R.drawable.ic_settings_check else 0)
//        binding.bottomSheetTheme.imgLight.setBackgroundResource(if (theme == ThemePref.LIGHT) R.drawable.background_selected else R.drawable.background_edit_text)
//        binding.bottomSheetTheme.imgDark.setImageResource(if (theme == ThemePref.DARK) R.drawable.ic_settings_check else 0)
//        binding.bottomSheetTheme.imgDark.setBackgroundResource(if (theme == ThemePref.DARK) R.drawable.background_selected else R.drawable.background_edit_text)
//    }
//
//    companion object {
//        fun intent(context: Context): Intent {
//            return Intent(context, SettingsActivity::class.java)
//        }
//    }
//}