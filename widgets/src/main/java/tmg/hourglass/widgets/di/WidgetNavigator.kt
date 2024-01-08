package tmg.hourglass.widgets.di

import android.content.Context
import android.content.Intent

interface WidgetNavigator {
    fun getIntent(context: Context): Intent
}