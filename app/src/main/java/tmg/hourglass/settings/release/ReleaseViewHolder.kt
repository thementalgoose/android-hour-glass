package tmg.hourglass.settings.release

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import tmg.hourglass.ReleaseNotes
import tmg.hourglass.databinding.ElementReleaseNotesBinding
import tmg.utilities.extensions.views.context

class ReleaseViewHolder(
    private val binding: ElementReleaseNotesBinding
): RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(releaseNote: ReleaseNotes) {
        binding.title.text = "${releaseNote.versionName} ${context.getString(releaseNote.title)}"
        binding.content.setText(releaseNote.release)
    }
}