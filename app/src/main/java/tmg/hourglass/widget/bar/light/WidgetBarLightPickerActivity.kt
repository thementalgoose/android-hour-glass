package tmg.hourglass.widget.bar.light

import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.widget.ItemWidgetPickerActivity

@AndroidEntryPoint
class WidgetBarLightPickerActivity: ItemWidgetPickerActivity<WidgetBarLightProvider>() {

    override val zClass: Class<WidgetBarLightProvider> = WidgetBarLightProvider::class.java
}