package tmg.hourglass.settings.privacy

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import org.koin.android.ext.android.inject
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.extensions.setOnClickListener
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class PrivacyPolicyActivity: BaseActivity() {

    private val viewModel: PrivacyPolicyViewModel by inject()

    override fun layoutId(): Int = R.layout.activity_privacy_policy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tvPolicy.text = getString(R.string.privacy_policy_data).fromHtml()

        ibtnClose.setOnClickListener(viewModel.inputs::clickBack)

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }
    }
}