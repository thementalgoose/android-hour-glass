package tmg.hourglass.settings.release

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.databinding.ActivityReleaseNotesBinding
import tmg.hourglass.extensions.setOnClickListener
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class ReleaseActivity: BaseActivity() {

    private lateinit var binding: ActivityReleaseNotesBinding
    private lateinit var adapter: ReleaseAdapter

    private val viewModel: ReleaseViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReleaseNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibtnClose.setOnClickListener(viewModel.inputs::clickBack)

        adapter = ReleaseAdapter()
        binding.content.adapter = adapter
        binding.content.layoutManager = LinearLayoutManager(this)

        observe(viewModel.outputs.content) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.goBack) {
            finish()
        }
    }
}