package tmg.hourglass.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_item_widget_picker.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.extensions.setOnClickListener
import tmg.hourglass.extensions.updateWidget
import tmg.hourglass.home.HomeAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.views.show

abstract class ItemWidgetPickerActivity<T : AppWidgetProvider> : BaseActivity() {

    abstract val zClass: Class<T>

    private val viewModel: ItemWidgetPickerViewModel by viewModel()
    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    private lateinit var adapter: HomeAdapter

    override fun layoutId(): Int = R.layout.activity_item_widget_picker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = HomeAdapter(
            actionItem = { id, _ ->
                viewModel.inputs.checkedItem(id)
            }
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        appWidgetId = getWidgetId()
        viewModel.inputs.supplyAppWidgetId(appWidgetId)

        btnSave.setOnClickListener(viewModel.inputs::clickSave)

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observe(viewModel.outputs.isSavedEnabled) {
            btnSave.show(it)
        }

        observeEvent(viewModel.outputs.save) {
            updateWidget(it)
        }
    }

    private fun getWidgetId(): Int = intent.extras?.getInt(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID
    ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

    private fun updateWidget(toId: String) {

        updateWidget(zClass, appWidgetId)

        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, toId)
        }
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }
}