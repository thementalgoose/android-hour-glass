package tmg.passage.home

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.passage.R
import tmg.passage.base.BaseActivity
import tmg.passage.modify.ModifyActivity
import tmg.passage.settings.SettingsActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class HomeActivity: BaseActivity() {

    private val viewModel: HomeViewModel by viewModel()
    private val adapter: HomeAdapter = HomeAdapter()

    override fun layoutId(): Int = R.layout.activity_home

    override fun initViews() {
        super.initViews()

        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)

        bnvNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_now -> {

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_previous -> {

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    return@setOnNavigationItemSelectedListener false
                }
                else -> false
            }
        }

        ibtnAdd.setOnClickListener {
            viewModel.inputs.clickAdd()
        }

        observe(viewModel.outputs.items) {
            adapter.list = it
            adapter.notifyDataSetChanged()
        }

        observeEvent(viewModel.outputs.addItemEvent) {
            startActivity(ModifyActivity.intent(this))
        }
    }
}