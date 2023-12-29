package tmg.hourglass.widgets.presentation.configure

import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class CountdownConfigurationActivity: AppCompatActivity() {

    private val appWidgetId by lazy {
        intent?.extras?.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID) ?: INVALID_APPWIDGET_ID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val value = Intent().putExtra(EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, value)

//        viewModel.inputs.load(appWidgetId)

        setContent {

        }
    }


    override fun onStop() {
//        viewModel.inputs.update()
        super.onStop()
    }
}