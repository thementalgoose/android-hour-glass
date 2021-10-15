package tmg.hourglass.home.views

import android.graphics.Color
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import tmg.hourglass.R
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.databinding.ElementCountdownItemBinding
import tmg.hourglass.home.HomeItemAction
import tmg.hourglass.home.HomeItemType
import tmg.hourglass.utils.ProgressUtils.Companion.getProgress
import tmg.utilities.extensions.format
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.show
import kotlin.math.floor

class ItemViewHolder(
    private val binding: ElementCountdownItemBinding,
    val actionItem: (id: String, action: HomeItemAction) -> Unit
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var passageId: String
    private lateinit var action: HomeItemAction

    fun bind(item: HomeItemType.Item) {
        passageId = item.countdown.id
        action = item.action
        val countdown: Countdown = item.countdown

        itemView.apply {
            if (item.clickBackground) {
                binding.clMain.setOnClickListener(this@ItemViewHolder)
            }
            else {
                binding.ibtnEdit.setOnClickListener(this@ItemViewHolder)
            }
            binding.clMain.isClickable = item.clickBackground
            binding.clMain.isFocusable = item.clickBackground
            binding.ibtnEdit.isClickable = !item.clickBackground
            binding.ibtnEdit.isFocusable = !item.clickBackground
            binding.ibtnEdit.isEnabled = !item.clickBackground
            binding.tvTitle.text = countdown.name

            val start: Int = countdown.initial.toIntOrNull() ?: 0
            val end: Int = countdown.finishing.toIntOrNull() ?: 100

            binding.tvDescription.show(item.showDescription)
            if (countdown.description.isEmpty()) {
                binding.tvDescription.text = context.getString(
                    R.string.home_no_description,
                    countdown.countdownType.converter(start.toString()),
                    countdown.start.format("dd MMM yyyy"),
                    countdown.countdownType.converter(end.toString()),
                    countdown.end.format("dd MMM yyyy")
                ).fromHtml()
            } else {
                binding.tvDescription.text = context.getString(
                    R.string.home_description,
                    countdown.description,
                    countdown.countdownType.converter(start.toString()),
                    countdown.start.format("dd MMM yyyy"),
                    countdown.countdownType.converter(end.toString()),
                    countdown.end.format("dd MMM yyyy")
                ).fromHtml()
            }

            binding.ibtnEdit.setImageResource(action.id)
            if (item.isEnabled) {
                binding.ibtnEdit.setBackgroundResource(R.drawable.background_selected)
            }
            else {
                binding.ibtnEdit.setBackgroundColor(itemView.context.theme.getColor(R.attr.pBackgroundPrimary))
            }

            binding.lpvMain.backgroundColour = itemView.context.theme.getColor(R.attr.pBackgroundSecondary)
            binding.lpvMain.progressColour = countdown.colour.toColorInt()
            binding.lpvMain.textBarColour = Color.WHITE
            binding.lpvMain.textBackgroundColour = itemView.context.theme.getColor(R.attr.pTextSecondary)
            val progress = getProgress(countdown.startByType, countdown.endByType, interpolator = countdown.interpolator)
            when {
                progress == 0.0f -> {
                    binding.lpvMain.setProgress(0.01f) { countdown.countdownType.converter(start.toString()) }
                }
                item.animateBar -> {
                    binding.lpvMain.animateProgress(progress) {
                        countdown.countdownType.converter(floor((start + (it * (end - start)))).toInt().toString())
                    }
                }
                else -> {
                    binding.lpvMain.setProgress(progress) {
                        countdown.countdownType.converter(floor((start + (it * (end - start)))).toInt().toString())
                    }
                }
            }
        }
    }

    //region OnClickListener

    override fun onClick(p0: View?) {
        actionItem(passageId, action)
    }

    //endregion
}