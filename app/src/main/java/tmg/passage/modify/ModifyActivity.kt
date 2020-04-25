package tmg.passage.modify

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_modify.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnFastChooseColorListener
import tmg.passage.R
import tmg.passage.base.BaseActivity
import tmg.passage.extensions.hexColour
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent


class ModifyActivity : BaseActivity(), OnFastChooseColorListener {

    private val viewModel: ModifyViewModel by viewModel()
    private var passageId: String? = null

    private var currentColour: String? = null

    override fun layoutId(): Int = R.layout.activity_modify

    override fun arguments(bundle: Bundle) {
        passageId = bundle.getString(keyPassageId)
        viewModel.inputs.initialise(passageId)
    }

    override fun initViews() {

        ibtnClose.setOnClickListener {
            viewModel.inputs.clickClose()
        }
        btnDates.setOnClickListener {
            showRangePicker()
        }
        btnColour.setOnClickListener {
            showColourPicker(currentColour ?: "#f84c44")
        }


        observe(viewModel.outputs.colour) {
            this.currentColour = it
        }


        observeEvent(viewModel.outputs.closeEvent) {
            finish()
        }
        observe(viewModel.outputs.isAddition) {
            ibtnDelete.isGone = it
            tvHeader.setText(if (it) R.string.modify_header_add else R.string.modify_header_edit)
        }
    }

    private fun showColourPicker(withDefault: String) {
        ColorPicker(this)
            .setOnFastChooseColorListener(this)
            .setDefaultColorButton(Color.parseColor(withDefault))
            .setColumns(5)
            .show()
    }

    private fun showRangePicker() {
        val picker = MaterialDatePicker.Builder.dateRangePicker().build()
        picker.addOnPositiveButtonClickListener {
            Toast.makeText(applicationContext, "RANGE PICKER", Toast.LENGTH_SHORT).show()
        }
        picker.show(supportFragmentManager, picker.toString())
    }

    companion object {

        private const val keyPassageId: String = "keyPassageId"

        fun intent(context: Context, passageId: String? = null): Intent {
            val intent = Intent(context, ModifyActivity::class.java)
            intent.putExtra(keyPassageId, passageId)
            return intent
        }
    }

    //region OnFastChooseColorListener

    override fun setOnFastChooseColorListener(position: Int, color: Int) {
        viewModel.inputs.inputColour(color.hexColour)
    }

    override fun onCancel() {}

    //endregion
}