package tmg.hourglass.widget.circle.light

import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.widget.ItemWidgetPickerActivity

@AndroidEntryPoint
class WidgetCircleLightPickerActivity: ItemWidgetPickerActivity<WidgetCircleLightProvider>() {

    override val zClass: Class<WidgetCircleLightProvider> = WidgetCircleLightProvider::class.java
}