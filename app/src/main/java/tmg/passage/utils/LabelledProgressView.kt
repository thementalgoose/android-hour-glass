package tmg.passage.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import tmg.passage.R
import tmg.utilities.extensions.dpToPx
import tmg.utilities.extensions.pxToDp
import tmg.utilities.utils.DensityUtils.Companion.toPx
import kotlin.math.max
import kotlin.math.min

/**
 * Copied from initial implementation as CorrectPercentProgressView from Transmission
 */
class LabelledProgressView: View, ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private var backgroundPaint: Paint = Paint()
    private var progressPaint: Paint = Paint()
    private var textPaint: Paint = Paint()
    private lateinit var valueAnimator: ValueAnimator

    private var canvasWidth: Float = 0.0f
    private var canvasHeight: Float = 0.0f
    private var radiusPxToDp: Float = 0.0f

    var backgroundColour: Int = Color.WHITE
        set(value) {
            backgroundPaint.color = value
            field = value
        }
    var progressColour: Int = Color.BLACK
        set(value) {
            progressPaint.color = value
            field = value
        }
    var textColour: Int = Color.WHITE
        set(value) {
            textPaint.color = value
            field = value
        }
    var timeLimit: Int = 1200
    private var startImmediately: Boolean = false

    private var progressPercentage: Float = 0.0f
    private var firstRun: Boolean = true
    private var callback: CorrectPercentProgressViewCallback? = null

    private var labelResolver: (progress: Float) -> String = { "${(it * 100).toInt()}%" }

    constructor(context: Context?) : super(context) {
        initView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attributeSet: AttributeSet? = null) {
        context
            .theme
            .obtainStyledAttributes(attributeSet, R.styleable.LabelledProgressView, 0, 0)
            .apply {
                try {
                    backgroundColour = getColor(R.styleable.LabelledProgressView_lpv_backgroundColour, backgroundColour)
                    progressColour = getColor(R.styleable.LabelledProgressView_lpv_progressColour, progressColour)
                    textColour = getColor(R.styleable.LabelledProgressView_lpv_textColour, textColour)
                    timeLimit = getInt(R.styleable.LabelledProgressView_lpv_timeLimit, timeLimit)
                    startImmediately = getBoolean(R.styleable.LabelledProgressView_lpv_startImmediately, startImmediately)
                }
                finally {
                    recycle()
                }
            }
    }

    private fun initVariables(){
        textPaint.textSize = 16.dpToPx(context.resources)

        radiusPxToDp = 0.pxToDp(context.resources)
        canvasWidth = width.toFloat()
        canvasHeight = height.toFloat()

        firstRun = false
    }

    //region Public methods for call

    fun setCallback(callback: CorrectPercentProgressViewCallback) {
        this.callback = callback
    }

    fun setProgress(progress: Float, resolver: ((progress: Float) -> String) = { "${(it * 100).toInt()}%" }) {
        start(progress)
        this.labelResolver = resolver
    }

    //endregion

    private fun start(withProgress: Float) {
        val prog: Float = max(0.01f, min(1.0f, withProgress))
        valueAnimator = ValueAnimator.ofFloat(0.0f, prog)
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = (timeLimit.toLong())
        valueAnimator.addUpdateListener(this)
        valueAnimator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (firstRun) {
            initVariables()
            if (startImmediately) {
                start(.5f)
            }
        }

        val percentage: String = labelResolver(progressPercentage)
        val textWidth: Float = textPaint.measureText(percentage)

        canvas?.drawRoundRect(0f, 0f, canvasWidth, canvasHeight, radiusPxToDp, radiusPxToDp, backgroundPaint)
        canvas?.drawRoundRect(0f, 0f, progressPercentage * canvasWidth, canvasHeight, radiusPxToDp, radiusPxToDp, progressPaint)

        val textY: Float = (canvasHeight - textPaint.fontMetrics.bottom)

        canvas?.drawText(percentage, (progressPercentage * canvasWidth) - toPx(resources, 8.toFloat()) - textWidth, textY, textPaint)
    }

    //region Animator.AnimatorListener

    override fun onAnimationStart(animation: Animator?) {

    }

    override fun onAnimationCancel(animation: Animator?) {

    }

    override fun onAnimationEnd(animation: Animator?) {

    }

    override fun onAnimationRepeat(animation: Animator?) {

    }

    //endregion

    //region ValueAnimator.AnimatorUpdateListener

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        if (animation == valueAnimator) {
            progressPercentage = animation.animatedValue as Float
            invalidate()
        }
    }

    //endregion
}

//region CorrectPercentProgressView Callback interface

interface CorrectPercentProgressViewCallback {

    fun animationFinished()

}

//endregion