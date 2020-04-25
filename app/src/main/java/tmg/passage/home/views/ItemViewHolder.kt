package tmg.passage.home.views

import android.graphics.Color
import android.view.View
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.element_passage_item.view.*
import tmg.passage.R
import tmg.passage.data.models.Passage
import tmg.passage.home.HomeItemType
import tmg.utilities.extensions.getColor
import kotlin.random.Random.Default.nextFloat

class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(passage: HomeItemType.Item) {
        itemView.apply {
            tvTitle.text = passage.passage.name
            tvDescription.text = passage.passage.description

            val start = 16_000
            val end = 40_000

            lpvMain.backgroundColour = itemView.context.theme.getColor(R.attr.pBackgroundSecondary)
            lpvMain.progressColour = Color.GREEN
            lpvMain.setProgress(nextFloat()) {
                (start + (it * (end - start))).toInt().toString()
            }
        }
    }
}