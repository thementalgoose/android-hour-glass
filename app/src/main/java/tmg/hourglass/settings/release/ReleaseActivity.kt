package tmg.hourglass.settings.release

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_release_notes.*
import org.koin.android.ext.android.inject
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.extensions.setOnClickListener
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class ReleaseActivity: BaseActivity() {

    private val viewModel: ReleaseViewModel by inject()

    override fun layoutId(): Int = R.layout.activity_release_notes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ibtnClose.setOnClickListener(viewModel.inputs::clickBack)

        observe(viewModel.outputs.content) {
            tvReleaseNotes.text = it.joinToString(separator = "<br/><br/>") { getString(it) }
                .fromHtml()
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }
    }
}