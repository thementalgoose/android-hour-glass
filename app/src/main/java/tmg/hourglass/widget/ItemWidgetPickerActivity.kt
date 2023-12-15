package tmg.hourglass.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import tmg.hourglass.extensions.updateWidget
import tmg.hourglass.presentation.AppTheme

abstract class ItemWidgetPickerActivity<T : AppWidgetProvider> : AppCompatActivity() {

    abstract val zClass: Class<T>

    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appWidgetId = getWidgetId()
        setContent {
            AppTheme {
                Scaffold(content = {
                    ItemWidgetPickerScreenVM(
                        appWidgetId = appWidgetId,
                        actionUpClicked = {
                            finish()
                        },
                        saveClicked = {
                            updateWidget(it)
                            finish()
                        }
                    )
                })
            }
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