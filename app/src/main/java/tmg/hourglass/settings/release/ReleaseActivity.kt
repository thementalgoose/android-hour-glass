package tmg.hourglass.settings.release

import kotlinx.android.synthetic.main.activity_release_notes.*
import org.koin.android.ext.android.inject
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe

class ReleaseActivity: BaseActivity() {

    private val viewModel: ReleaseViewModel by inject()

    override fun layoutId(): Int = R.layout.activity_release_notes

    override fun initViews() {
        observe(viewModel.outputs.content) {
            tvReleaseNotes.text = it.joinToString(separator = "<br/><br/>") { getString(it) }
                .fromHtml()
        }
    }
}