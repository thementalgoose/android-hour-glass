package tmg.hourglass.modify

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.graphics.toColorInt
import androidx.core.util.Pair
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment
import kotlinx.android.synthetic.main.activity_modify.*
import kotlinx.android.synthetic.main.bottom_sheet_interpolator.*
import kotlinx.android.synthetic.main.bottom_sheet_modify.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnFastChooseColorListener
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.data.CountdownInterpolator
import tmg.hourglass.data.CountdownType
import tmg.hourglass.extensions.*
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.*
import tmg.utilities.extensions.views.show
import java.util.*


class ModifyActivity : BaseActivity(), OnFastChooseColorListener,
    MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>?> {

    private val viewModel: ModifyViewModel by viewModel()
    private var passageId: String? = null

    private lateinit var typeBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var typeAdapter: ModifyTypeAdapter<CountdownType>

    private lateinit var interpolatorBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var interpolatorAdapter: ModifyTypeAdapter<CountdownInterpolator>

    private var currentColour: String? = null

    override fun layoutId(): Int = R.layout.activity_modify

    override fun arguments(bundle: Bundle) {
        passageId = bundle.getString(keyPassageId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupTypeBottomSheet()
        setupInterpolatorBottomSheet()
        bottomSheetBackground.setOnClickListener { hideAllBottomSheets() }

        ibtnClose.setOnClickListener(viewModel.inputs::clickClose)
        btnType.setOnClickListener {
            typeBottomSheet.expand()
        }
        btnInterpolator.setOnClickListener {
            interpolatorBottomSheet.expand()
        }
        btnColour.setOnClickListener {
            showColourPicker(currentColour ?: "#f84c44")
        }
        btnDates.setOnClickListener(viewModel.inputs::clickDatePicker)
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
                etInitial.setText(it)
            }
        }
        observe(viewModel.outputs.final) {
            if (!etFinal.hasFocus()) {
                etFinal.setText(it)
            }
        }

        observe(viewModel.outputs.colour) {
            this.currentColour = it
            vColour.setBackgroundColor(it.toColorInt())
        }

        observe(viewModel.outputs.typeList) {
            typeAdapter.list = it
        }
        observe(viewModel.outputs.type) {
            btnType.setText(it.label())
        }

        observe(viewModel.outputs.interpolatorList) {
            interpolatorAdapter.list = it
        }
        observe(viewModel.outputs.interpolator) {
            btnInterpolator.setText(it.label())
        }

        observeEvent(viewModel.outputs.showDatePicker) { dates ->
            showRangePicker(dates?.first, dates?.second)
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

        observe(viewModel.outputs.showRange) { (from, to) -> showRange(from, to) }

        viewModel.inputs.initialise(passageId)
    }

    private fun setupTypeBottomSheet() {
        typeBottomSheet = BottomSheetBehavior.from(bsModifyType)
        typeBottomSheet.isHideable = true
        typeBottomSheet.hidden()
        typeBottomSheet.addBottomSheetCallback(BottomSheetFader(bottomSheetBackground, "type"))

        typeAdapter = ModifyTypeAdapter(
            itemSelected = {
                viewModel.inputs.inputType(it.toEnum<CountdownType> { it.key }!!)
                typeBottomSheet.hidden()
            }
        )
        rvOptions.adapter = typeAdapter
        rvOptions.layoutManager = LinearLayoutManager(this)
    }

    private fun setupInterpolatorBottomSheet() {
        interpolatorBottomSheet = BottomSheetBehavior.from(bsInterpolator)
        interpolatorBottomSheet.isHideable = true
        interpolatorBottomSheet.hidden()
        interpolatorBottomSheet.addBottomSheetCallback(BottomSheetFader(bottomSheetBackground, "interpolator"))

        interpolatorAdapter = ModifyTypeAdapter(
            itemSelected = {
                viewModel.inputs.inputInterpolator(it.toEnum<CountdownInterpolator> { it.key }!!)
                interpolatorBottomSheet.hidden()
            }
        )
        rvInterpolatorOptions.adapter = interpolatorAdapter
        rvInterpolatorOptions.layoutManager = LinearLayoutManager(this)
    }

    private fun showColourPicker(withDefault: String) {
        ColorPicker(this)
            .setOnFastChooseColorListener(this)
            .setDefaultColorButton(Color.parseColor(withDefault))
            .setColumns(5)
            .show()
    }

    private fun showRange(from: Boolean, to: Boolean) {
        tvFieldRange.show(from || to)
        tvFieldRangeDesc.show(from || to)
        etInitial.show(from)
        etFinal.show(to)
    }

    private fun showRangePicker(from: LocalDateTime?, to: LocalDateTime?) {

        val builder = MaterialDatePicker.Builder.dateRangePicker()
        if (from != null && to != null) {
            builder.setSelection(Pair(from.millis, to.millis))
        }
        builder.setTitleText(getString(R.string.modify_field_dates))
        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener(this)
    }

    //region OnFastChooseColorListener

    override fun setOnFastChooseColorListener(position: Int, color: Int) {
        viewModel.inputs.inputColour(color.hexColor)
    }

    override fun onCancel() {}

    //endregion

    override fun onBackPressed() {
        if (typeBottomSheet.state != BottomSheetBehavior.STATE_HIDDEN ||
                interpolatorBottomSheet.state != BottomSheetBehavior.STATE_HIDDEN) {
            hideAllBottomSheets()
        }
        else {
            super.onBackPressed()
        }
    }

    private fun hideAllBottomSheets() {
        typeBottomSheet.hidden()
        interpolatorBottomSheet.hidden()
    }

    companion object {

        private const val keyPassageId: String = "keyPassageId"

        fun intent(context: Context, passageId: String? = null): Intent {
            val intent = Intent(context, ModifyActivity::class.java)
            intent.putExtra(keyPassageId, passageId)
            return intent
        }
    }

    //region MaterialPickerOnPositiveButtonClickListener

    override fun onPositiveButtonClick(selection: Pair<Long, Long>?) {
        if (selection?.first != null && selection.second != null) {
            val start = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.first!!), ZoneId.systemDefault())
            val end = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.second!!), ZoneId.systemDefault())
            viewModel.inputs.inputDates(start, end)
        }
    }

    //endregion
}