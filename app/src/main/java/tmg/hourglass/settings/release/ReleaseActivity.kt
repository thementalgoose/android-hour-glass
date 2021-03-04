package tmg.hourglass.settings.release

import android.os.Bundle
import org.koin.android.ext.android.inject
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.databinding.ActivityReleaseNotesBinding
import tmg.hourglass.extensions.setOnClickListener
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class ReleaseActivity: BaseActivity() {

    private lateinit var binding: ActivityReleaseNotesBinding
    private val viewModel: ReleaseViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReleaseNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibtnClose.setOnClickListener(viewModel.inputs::clickBack)

        observe(viewModel.outputs.content) {
            binding.tvReleaseNotes.text = it.joinToString(separator = "<br/><br/>") { getString(it) }
                .fromHtml()
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }
    }
}