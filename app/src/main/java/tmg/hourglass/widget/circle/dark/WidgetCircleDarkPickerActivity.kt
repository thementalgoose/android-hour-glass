package tmg.hourglass.widget.circle.dark

import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.widget.ItemWidgetPickerActivity
import tmg.hourglass.widget.bar.light.WidgetBarLightProvider

@AndroidEntryPoint
class WidgetCircleDarkPickerActivity: ItemWidgetPickerActivity<WidgetCircleDarkProvider>() {

    override val zClass: Class<WidgetCircleDarkProvider> = WidgetCircleDarkProvider::class.java
}