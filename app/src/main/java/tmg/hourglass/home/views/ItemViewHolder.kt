package tmg.hourglass.home.views

import android.graphics.Color
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.element_countdown_item.view.*
import tmg.hourglass.R
import tmg.hourglass.data.models.Countdown
import tmg.hourglass.extensions.format
import tmg.hourglass.home.HomeItemAction
import tmg.hourglass.home.HomeItemType
import tmg.hourglass.utils.ProgressUtils.Companion.getProgress
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.getColor

class ItemViewHolder(
    itemView: View,
    val actionItem: (id: String, action: HomeItemAction) -> Unit
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.ibtnEdit.setOnClickListener(this)
    }

    private lateinit var passageId: String
    private lateinit var action: HomeItemAction

    fun bind(item: HomeItemType.Item) {
        passageId = item.countdown.id
        action = item.action
        val countdown: Countdown = item.countdown

        itemView.apply {
            tvTitle.text = countdown.name

            val start: Int = countdown.initial.toIntOrNull() ?: 0
            val end: Int = countdown.finishing.toIntOrNull() ?: 100

            if (countdown.description.isEmpty()) {
                tvDescription.text = context.getString(R.string.home_no_description,
                    start.toString(), countdown.start.format("dd MMM yyyy"),
                    end.toString(), countdown.end.format("dd MMM yyyy")
                ).fromHtml()
            }
            else {
                tvDescription.text = context.getString(R.string.home_description,
                    countdown.description,
                    start.toString(), countdown.start.format("dd MMM yyyy"),
                    end.toString(), countdown.end.format("dd MMM yyyy")
                ).fromHtml()
            }

            ibtnEdit.setImageResource(action.id)

            lpvMain.backgroundColour = itemView.context.theme.getColor(R.attr.pBackgroundSecondary)
            lpvMain.progressColour = countdown.colour.toColorInt()
            lpvMain.textBarColour = Color.WHITE
            lpvMain.textBackgroundColour = itemView.context.theme.getColor(R.attr.pTextSecondary)
            lpvMain.setProgress(getProgress(countdown.start, countdown.end)) {
                countdown.countdownType.converter((start + (it * (end - start))).toInt().toString())
            }
        }
    }

    //region OnClickListener

    override fun onClick(p0: View?) {
        actionItem(passageId, action)
    }

    //endregion
}