package tmg.hourglass.settings.privacy

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import tmg.hourglass.R
import tmg.hourglass.presentation.AppTheme
import tmg.hourglass.presentation.AppThemePreview
import tmg.hourglass.presentation.layouts.TitleBar
import tmg.hourglass.presentation.textviews.TextBody2

@Composable
fun PrivacyPolicyLayout(
    backClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        TitleBar(
            title = stringResource(id = R.string.settings_help_privacy_policy_title),
            backClicked = backClicked
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingLarge
            )) {
            val textColor = AppTheme.colors.textPrimary.toArgb()
            AndroidView(factory = { context ->
                TextView(context).apply {
                    setTextColor(textColor)
                    text = HtmlCompat.fromHtml(context.getString(R.string.privacy_policy_data), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    movementMethod = LinkMovementMethod.getInstance()
                }
            })
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview(isLight = true) {
        PrivacyPolicyLayout(
            backClicked = { }
        )
    }
}