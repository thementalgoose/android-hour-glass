package tmg.hourglass.modify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.base.BaseActivity
import tmg.hourglass.presentation.AppTheme

@AndroidEntryPoint
class ModifyActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent?.extras?.getString(keyCountdownId)

        setContent {
            AppTheme {
                ModifyScreenVM(
                    id = id,
                    isEdit = id != null,
                    actionUpClicked = {
                        finish()
                    }
                )
            }
        }
    }
    companion object {

        private const val keyCountdownId: String = "countdownId"

        fun intent(context: Context, passageId: String? = null): Intent {
            val intent = Intent(context, ModifyActivity::class.java)
            intent.putExtra(keyCountdownId, passageId)
            return intent
        }
    }
}