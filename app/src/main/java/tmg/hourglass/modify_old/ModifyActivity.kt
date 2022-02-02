package tmg.hourglass.modify_old

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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import org.koin.android.viewmodel.ext.android.viewModel
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnFastChooseColorListener
import tmg.hourglass.R
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.databinding.ActivityModifyBinding
import tmg.hourglass.extensions.*
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.*
import tmg.utilities.extensions.views.show


class ModifyActivity : BaseActivity(), OnFastChooseColorListener,
    MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>?> {

    private lateinit var binding: ActivityModifyBinding

    private val viewModel: ModifyViewModel by viewModel()
    private var passageId: String? = null

    private lateinit var typeBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var typeAdapter: ModifyTypeAdapter<CountdownType>

    private lateinit var interpolatorBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var interpolatorAdapter: ModifyTypeAdapter<CountdownInterpolator>

    private var currentColour: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.extras?.let {
            passageId = it.getString(keyPassageId)
        }

        setupTypeBottomSheet()
        setupInterpolatorBottomSheet()
        binding.bottomSheetBackground.setOnClickListener { hideAllBottomSheets() }

        binding.ibtnClose.setOnClickListener(viewModel.inputs::clickClose)
        binding.btnType.setOnClickListener {
            typeBottomSheet.expand()
        }
        binding.btnInterpolator.setOnClickListener {
            interpolatorBottomSheet.expand()
        }
        binding.btnColour.setOnClickListener {
            showColourPicker(currentColour ?: "#f84c44")
        }
        binding.btnDates.setOnClickListener(viewModel.inputs::clickDatePicker)
        binding.etFieldName.addTextUpdateListener(viewModel.inputs::inputName)
        binding.etFieldDescription.addTextUpdateListener(viewModel.inputs::inputDescription)
        binding.etInitial.addTextUpdateListener(viewModel.inputs::inputInitial)
        binding.etFinal.addTextUpdateListener(viewModel.inputs::inputFinal)

        binding.btnSave.setOnClickListener(viewModel.inputs::clickSave)
        binding.ibtnDelete.setOnClickListener(viewModel.inputs::clickDelete)



        observe(viewModel.outputs.name) {
            if (!binding.etFieldName.hasFocus()) {
                binding.etFieldName.setText(it.toString())
            }
        }
        observe(viewModel.outputs.description) {
            if (!binding.etFieldDescription.hasFocus()) {
                binding.etFieldDescription.setText(it.toString())
            }
        }
        observe(viewModel.outputs.initial) {
            if (!binding.etInitial.hasFocus()) {
                binding.etInitial.setText(it)
            }
        }
        observe(viewModel.outputs.final) {
            if (!binding.etFinal.hasFocus()) {
                binding.etFinal.setText(it)
            }
        }

        observe(viewModel.outputs.colour) {
            this.currentColour = it
            binding.vColour.setBackgroundColor(it.toColorInt())
        }

        observe(viewModel.outputs.typeList) {
            typeAdapter.list = it
        }
        observe(viewModel.outputs.type) {
            binding.btnType.setText(it.label())
        }

        observe(viewModel.outputs.interpolatorList) {
            interpolatorAdapter.list = it
        }
        observe(viewModel.outputs.interpolator) {
            binding.btnInterpolator.setText(it.label())
        }

        observeEvent(viewModel.outputs.showDatePicker) { dates ->
            showRangePicker(dates?.first, dates?.second)
        }
        observe(viewModel.outputs.dates) {
            binding.btnDates.text = getString(R.string.modify_field_dates_format, it.first.format("dd MMM yyyy"), it.second.format("dd MMM yyyy"))
        }

        observe(viewModel.outputs.isValid) {
            binding.btnSave.isEnabled = it
        }

        observeEvent(viewModel.outputs.closeEvent) {
            finish()
        }
        observe(viewModel.outputs.isAddition) {
            binding.ibtnDelete.isGone = it
            binding.tvHeader.setText(if (it) R.string.modify_header_add else R.string.modify_header_edit)
        }

        observe(viewModel.outputs.showRange) { (from, to) -> showRange(from, to) }

        viewModel.inputs.initialise(passageId)
    }

    private fun setupTypeBottomSheet() {
//        typeBottomSheet = BottomSheetBehavior.from(binding.bottomSheetModify.bsModifyType)
//        typeBottomSheet.isHideable = true
//        typeBottomSheet.hidden()
//        typeBottomSheet.addBottomSheetCallback(BottomSheetFader(binding.bottomSheetBackground, "type"))
//
//        typeAdapter = ModifyTypeAdapter(
//            itemSelected = {
//                viewModel.inputs.inputType(it.toEnum<CountdownType> { it.key }!!)
//                typeBottomSheet.hidden()
//            }
//        )
//        binding.bottomSheetModify.rvOptions.adapter = typeAdapter
//        binding.bottomSheetModify.rvOptions.layoutManager = LinearLayoutManager(this)
    }

    private fun setupInterpolatorBottomSheet() {
//        interpolatorBottomSheet = BottomSheetBehavior.from(binding.bottomSheetInterpolator.bsInterpolator)
//        interpolatorBottomSheet.isHideable = true
//        interpolatorBottomSheet.hidden()
//        interpolatorBottomSheet.addBottomSheetCallback(BottomSheetFader(binding.bottomSheetBackground, "interpolator"))
//
//        interpolatorAdapter = ModifyTypeAdapter(
//            itemSelected = {
//                viewModel.inputs.inputInterpolator(it.toEnum<CountdownInterpolator> { it.key }!!)
//                interpolatorBottomSheet.hidden()
//            }
//        )
//        binding.bottomSheetInterpolator.rvInterpolatorOptions.adapter = interpolatorAdapter
//        binding.bottomSheetInterpolator.rvInterpolatorOptions.layoutManager = LinearLayoutManager(this)
    }

    private fun showColourPicker(withDefault: String) {
        ColorPicker(this)
            .setOnFastChooseColorListener(this)
            .setDefaultColorButton(Color.parseColor(withDefault))
            .setColumns(5)
            .show()
    }

    private fun showRange(from: Boolean, to: Boolean) {
        binding.tvFieldRange.show(from || to)
        binding.tvFieldRangeDesc.show(from || to)
        binding.etInitial.show(from)
        binding.etFinal.show(to)
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
        println("Colour picked $position - ${color.hexColor}")
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
            val end = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection.second!!), ZoneId.systemDefault()).plusDays(1).minusSeconds(1L)
            viewModel.inputs.inputDates(start, end)
        }
    }

    //endregion
}