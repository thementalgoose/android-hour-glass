package tmg.hourglass.modify

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.graphics.toColorInt
import androidx.core.view.isGone
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment
import kotlinx.android.synthetic.main.activity_modify.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnFastChooseColorListener
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.extensions.addTextUpdateListener
import tmg.hourglass.extensions.format
import tmg.hourglass.extensions.hexColour
import tmg.hourglass.extensions.setOnClickListener
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent


class ModifyActivity : BaseActivity(), OnFastChooseColorListener,
    SmoothDateRangePickerFragment.OnDateRangeSetListener {

    private val viewModel: ModifyViewModel by viewModel()
    private var passageId: String? = null

    private var currentColour: String? = null

    override fun layoutId(): Int = R.layout.activity_modify

    override fun arguments(bundle: Bundle) {
        passageId = bundle.getString(keyPassageId)
        viewModel.inputs.initialise(passageId)
    }

    override fun initViews() {

        ibtnClose.setOnClickListener(viewModel.inputs::clickClose)
        btnDates.setOnClickListener {
            showRangePicker()
        }
        btnColour.setOnClickListener {
            showColourPicker(currentColour ?: "#f84c44")
        }
        etFieldName.addTextUpdateListener(viewModel.inputs::inputName)
        etFieldDescription.addTextUpdateListener(viewModel.inputs::inputDescription)
        etInitial.addTextUpdateListener(viewModel.inputs::inputInitial)
        etFinal.addTextUpdateListener(viewModel.inputs::inputFinal)

        btnSave.setOnClickListener(viewModel.inputs::clickSave)
        ibtnDelete.setOnClickListener(viewModel.inputs::clickDelete)

        observe(viewModel.outputs.name) {
            if (!etFieldName.hasFocus()) {
                etFieldName.setText(it.toString())
            }
        }
        observe(viewModel.outputs.description) {
            if (!etFieldDescription.hasFocus()) {
                etFieldDescription.setText(it.toString())
            }
        }
        observe(viewModel.outputs.initial) {
            if (!etInitial.hasFocus()) {
                etInitial.setText(it.toString())
            }
        }
        observe(viewModel.outputs.final) {
            if (!etFinal.hasFocus()) {
                etFinal.setText(it.toString())
            }
        }

        observe(viewModel.outputs.colour) {
            this.currentColour = it
            vColour.setBackgroundColor(it.toColorInt())
        }
        observe(viewModel.outputs.dates) {
            btnDates.text = getString(R.string.modify_field_dates_format, it.first.format("dd MMM yyyy"), it.second.format("dd MMM yyyy"))
        }

        observe(viewModel.outputs.isValid) {
            btnSave.isEnabled = it
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

        val fragment = SmoothDateRangePickerFragment.newInstance(this)
        fragment?.show(fragmentManager, "DatePicker")
    }

    //region OnFastChooseColorListener

    override fun setOnFastChooseColorListener(position: Int, color: Int) {
        viewModel.inputs.inputColour(color.hexColour)
    }

    override fun onCancel() {}

    //endregion

    companion object {

        private const val keyPassageId: String = "keyPassageId"

        fun intent(context: Context, passageId: String? = null): Intent {
            val intent = Intent(context, ModifyActivity::class.java)
            intent.putExtra(keyPassageId, passageId)
            return intent
        }
    }

    //region SmoothDateRangePickerFragment.OnDateRangeSetListener

    override fun onDateRangeSet(
        view: SmoothDateRangePickerFragment?,
        yearStart: Int,
        monthStart: Int,
        dayStart: Int,
        yearEnd: Int,
        monthEnd: Int,
        dayEnd: Int
    ) {
        val start: LocalDate = LocalDate.of(yearStart, monthStart + 1, dayStart)
        val end: LocalDate = LocalDate.of(yearEnd, monthEnd + 1, dayEnd)
        viewModel.inputs.inputDates(start.atStartOfDay(), end.plusDays(1).atStartOfDay().minusSeconds(1L))
    }

    //endregion
}