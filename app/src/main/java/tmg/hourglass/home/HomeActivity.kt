package tmg.hourglass.home

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.databinding.ActivityHomeBinding
import tmg.hourglass.extensions.updateAllWidgets
import tmg.hourglass.modify.ModifyActivity
import tmg.hourglass.settings.SettingsActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class HomeActivity: BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var adapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = HomeAdapter(
            actionItem = { id, action ->
                when (action) {
                    HomeItemAction.EDIT -> viewModel.inputs.editItem(id)
                    HomeItemAction.DELETE -> viewModel.inputs.deleteItem(id)
                    else -> {}
                }
            }
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        binding.bnvNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_now -> {
                    viewModel.inputs.switchList(HomeTab.NOW)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_previous -> {
                    viewModel.inputs.switchList(HomeTab.PREVIOUS)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    return@setOnNavigationItemSelectedListener false
                }
                else -> false
            }
        }

        binding.ibtnAdd.setOnClickListener {
            viewModel.inputs.clickAdd()
        }

        observe(viewModel.outputs.items) {
            adapter.list = it
            adapter.notifyDataSetChanged()
        }

        observeEvent(viewModel.outputs.addItemEvent) {
            startActivity(ModifyActivity.intent(this))
        }

        observeEvent(viewModel.outputs.editItemEvent) {
            startActivity(ModifyActivity.intent(this, it))
        }

        observeEvent(viewModel.outputs.deleteItemEvent) {
            updateAllWidgets()
        }
    }
}