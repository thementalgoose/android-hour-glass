package tmg.hourglass.presentation.modify

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import tmg.hourglass.BuildConfig
import tmg.hourglass.core.crashlytics.screenview.ScreenView
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.modify.layout.DataRangeDateLayout
import tmg.hourglass.presentation.modify.layout.DataRangeInputLayout
import tmg.hourglass.presentation.modify.layout.DataSingleDateLayout
import tmg.hourglass.presentation.modify.layout.NotificationsLayout
import tmg.hourglass.presentation.modify.layout.PersonaliseLayout
import tmg.hourglass.presentation.modify.layout.SaveLayout
import tmg.hourglass.presentation.modify.layout.TagLayout
import tmg.hourglass.presentation.modify.layout.TypeLayout
import tmg.hourglass.strings.R

@Composable
fun ModifyScreenVM(
    paddingValues: PaddingValues,
    windowSizeClass: WindowSizeClass,
    actionUpClicked: () -> Unit,
    navigateToTag: () -> Unit,
    countdownId: String?,
    viewModel: ModifyViewModel = hiltViewModel()
) {
    if (countdownId != null) {
        ScreenView("Modify", updateKey = countdownId)
    } else {
        ScreenView("Add")
    }
    DisposableEffect(countdownId) {
        Log.d("Modify", "Initialising VM with value $countdownId")
        viewModel.initialise(countdownId)
        return@DisposableEffect onDispose { }
    }

    val uiState = viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshNotificationsEnabled()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.refreshNotificationsEnabled()
        if (!isGranted) {
            openNotificationSettings(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(paddingValues),
    ) {
        TitleBar(
            titleModifier = Modifier.padding(start = AppTheme.dimensions.paddingMedium),
            title = stringResource(id = if (countdownId != null) R.string.modify_header_edit else R.string.modify_header_add),
            showBack = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
            actionUpClicked = actionUpClicked,
        )

        PersonaliseLayout(
            emoji = uiState.value.emoji,
            emojiPicked = viewModel::setEmoji,
            name = uiState.value.title,
            nameUpdated = viewModel::setTitle,
            nameError = uiState.value.errors.any { it == UiState.ErrorTypes.TITLE_BLANK },
            description = uiState.value.description,
            descriptionUpdated = viewModel::setDescription,
            color = uiState.value.colorHex,
            colorPicked = viewModel::setColor
        )

        TypeLayout(
            type = uiState.value.type,
            typeUpdated = viewModel::setType
        )

        when (val inputData = uiState.value.inputTypes) {
            is UiState.Types.EndDate -> {
                val errorString = when {
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_NULL } -> stringResource(R.string.modify_error_finish_date_null)
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_IN_PAST } -> stringResource(R.string.modify_error_finish_date_in_past)
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_INVALID } -> stringResource(R.string.modify_error_finish_invalid)
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_BEFORE_START_DATE } -> stringResource(R.string.modify_error_finish_date_before_start)
                    uiState.value.errors.any { it == UiState.ErrorTypes.START_DATE_IN_FUTURE } -> stringResource(R.string.modify_error_start_date_in_past)
                    else -> null
                }
                DataSingleDateLayout(
                    day = inputData.day,
                    month = inputData.month,
                    year = inputData.year,
                    dayUpdated = viewModel::setEndDateDay,
                    monthUpdated = viewModel::setEndDateMonth,
                    yearUpdated = viewModel::setEndDateYear,
                    startDate = inputData.startDate,
                    startDateUpdated = viewModel::setStartDate,
                    error = errorString
                )
            }
            is UiState.Types.Values -> {
                val errorDate = when {
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_NULL } -> stringResource(R.string.modify_error_finish_date_null)
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_IN_PAST } -> stringResource(R.string.modify_error_finish_date_in_past)
                    uiState.value.errors.any { it == UiState.ErrorTypes.START_DATE_NULL } -> stringResource(R.string.modify_error_start_date_null)
                    uiState.value.errors.any { it == UiState.ErrorTypes.FINISH_DATE_BEFORE_START_DATE } -> stringResource(R.string.modify_error_finish_date_before_start)
                    else -> null
                }
                DataRangeDateLayout(
                    startDate = inputData.startDate,
                    startDateUpdated = viewModel::setStartDate,
                    endDate = inputData.endDate,
                    endDateUpdated = viewModel::setEndDate,
                    error = errorDate
                )

                val errorValue = when {
                    uiState.value.errors.any { it == UiState.ErrorTypes.VALUES_EMPTY } -> stringResource(R.string.modify_error_value_empty)
                    uiState.value.errors.any { it == UiState.ErrorTypes.VALUES_MATCH } -> stringResource(R.string.modify_error_value_match)
                    uiState.value.errors.any { it == UiState.ErrorTypes.VALUES_MUST_BE_NUMBER } -> stringResource(R.string.modify_error_value_match)
                    else -> null
                }
                DataRangeInputLayout(
                    initial = inputData.startValue,
                    initialUpdated = viewModel::setStartValue,
                    finishing = inputData.endValue,
                    finishingUpdated = viewModel::setEndValue,
                    error = errorValue
                )
            }
        }

        NotificationsLayout(
            notifications = uiState.value.notifications,
            notificationsEnabled = uiState.value.notificationsEnabled,
            isValuesType = uiState.value.inputTypes is UiState.Types.Values,
            getNotificationError = uiState.value::getNotificationError,
            onUpdateValue = viewModel::updateNotificationValue,
            onUpdateType = viewModel::updateNotificationType,
            onDelete = viewModel::deleteNotification,
            onEnableNotifications = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    openNotificationSettings(context)
                }
            }
        )

        TagLayout(
            tags = uiState.value.allTags,
            selected = uiState.value.tag,
            selectTag = viewModel::setTag,
            navigateToTag = navigateToTag
        )

        SaveLayout(
            isEdit = countdownId != null,
            saveEnabled = uiState.value.isSaveEnabled,
            saveClicked = {
                viewModel.save()
                actionUpClicked()
            },
            deleteClicked = {
                viewModel.delete()
                actionUpClicked()
            },
            cancelClicked = actionUpClicked
        )

        Spacer(Modifier.imePadding())
    }
}

private fun openNotificationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}