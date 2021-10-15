package tmg.hourglass.settings.privacy

import android.os.Bundle
import android.text.method.LinkMovementMethod
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.databinding.ActivityPrivacyPolicyBinding
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observeEvent

class PrivacyPolicyActivity: BaseActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding
    private val viewModel: PrivacyPolicyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvPolicy.text = getString(R.string.privacy_policy_data).fromHtml()
        binding.tvPolicy.movementMethod = LinkMovementMethod.getInstance()

        binding.ibtnClose.setOnClickListener {
            viewModel.inputs.clickBack()
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }
    }
}