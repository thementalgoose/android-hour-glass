package tmg.hourglass.extensions

import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener

fun View.setOnClickListener(method: () -> Unit) {
    this.setOnClickListener {
        method()
    }
}

fun EditText.addTextUpdateListener(method: (text: String) -> Unit) {
    this.addTextChangedListener {
        if (this.hasFocus()) {
            method(it.toString())
        }
    }
}