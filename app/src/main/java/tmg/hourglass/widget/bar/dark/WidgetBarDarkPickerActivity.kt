package tmg.hourglass.widget.bar.dark

import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.widget.ItemWidgetPickerActivity

@AndroidEntryPoint
class WidgetBarDarkPickerActivity: ItemWidgetPickerActivity<WidgetBarDarkProvider>() {

    override val zClass: Class<WidgetBarDarkProvider> = WidgetBarDarkProvider::class.java
}