package tmg.hourglass.home.views

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.element_countdown_placeholder.view.*
import org.threeten.bp.LocalDate
import tmg.hourglass.R
import tmg.hourglass.extensions.format
import tmg.hourglass.extensions.millis
import tmg.hourglass.utils.LocalDateTimeUtils

class PlaceholderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

//    private val end: Int = 10_000

//    private val startDate: LocalDate = LocalDate.of(2020, 1, 1)
//    private val endDate: LocalDate = LocalDate.of(2022, 12, 31)
//    private val startMillis: Long = startDate.atStartOfDay().millis
//    private val diff = endDate.atStartOfDay().millis - startDate.atStartOfDay().millis

    fun bind() {
//        itemView.lpvMain.progressColour = ContextCompat.getColor(itemView.context, R.color.brand)
//        itemView.lpvMain.timeLimit = 8_000
//        itemView.lpvMain.setProgress(1.0f) {
//            "${date(it)} £${(it * end).toInt()}"
//        }
    }

//    private fun date(progress: Float): String {
//        return LocalDateTimeUtils.ofMillis((startMillis + (diff * progress)).toLong()).format("dd MMM yyyy")
//    }
}