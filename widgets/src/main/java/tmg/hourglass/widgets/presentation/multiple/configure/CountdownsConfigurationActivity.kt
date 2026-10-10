package tmg.hourglass.widgets.presentation.multiple.configure

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.widgets.presentation.multiple.CountdownsWidgetReceiver
import tmg.hourglass.widgets.updateAllWidgets
import tmg.utilities.extensions.updateWidget

@AndroidEntryPoint
class CountdownsConfigurationActivity : AppCompatActivity() {

    private val appWidgetId by lazy {
        intent?.extras?.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID) ?: INVALID_APPWIDGET_ID
    }

    private val viewModel: CountdownsConfigurationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        this.enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        viewModel.load(appWidgetId)

        setContent {
            AppTheme {
                CountdownsConfigurationScreenVM(
                    backClicked = ::update
                )
            }
        }
    }

    override fun onStop() {
        update()
        super.onStop()
    }

    private fun update() {
        Log.i("Widgets", "Countdowns Configuration Activity update - $appWidgetId")
        if (appWidgetId != INVALID_APPWIDGET_ID) {
            updateAllWidgets()
        } else {
            updateWidget<CountdownsWidgetReceiver>(appWidgetId)
        }
        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, appWidgetId)
        }
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }
}
