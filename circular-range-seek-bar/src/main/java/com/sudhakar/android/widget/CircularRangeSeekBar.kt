package com.sudhakar.android.widget

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import kotlin.properties.Delegates

class CircularRangeSeekBar : FrameLayout {


    private val DEFAULT_SEEKBAR_STYLE: Int = 0
    private val DEFAULT_CIRCLE_COLOR = Color.GRAY
    private val DEFAULT_CIRCLE_PROGRESS_COLOR = Color.DKGRAY
    private var circleProgressColor: Int = DEFAULT_CIRCLE_PROGRESS_COLOR
    private var circleColor: Int = DEFAULT_CIRCLE_COLOR
    val TAG = "CircularRangeSeekBar"

    constructor(context: Context)
            : super(context)

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0)
            : super(context, attributeSet, defStyleAttr) {
        val attrArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.RangeSeekBar,
            defStyleAttr,
            0
        )
        initAttributes(attrArray)

    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attributeSet, defStyleAttr, defStyleRes) {
        val attrArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.RangeSeekBar,
            defStyleAttr,
            0
        )
        initAttributes(attrArray)
    }


    init {
        isClickable = true
    }

    private fun initAttributes(attrArray: TypedArray) {
        seekBarStyle =
            attrArray.getInt(R.styleable.RangeSeekBar_rangeSeekBarStyle, DEFAULT_SEEKBAR_STYLE)
        circleProgressColor = attrArray.getColor(
            R.styleable.RangeSeekBar_circle_progress_color,
            DEFAULT_CIRCLE_PROGRESS_COLOR
        )
        circleColor =
            attrArray.getColor(R.styleable.RangeSeekBar_circle_color, DEFAULT_CIRCLE_COLOR)

        arcPaint.color = circleProgressColor
        circlePaint.color = circleColor
        Log.d(TAG, "seekBarStyle initAttributes : $seekBarStyle")
    }

    var seekBarChangeListener: OnSeekChangeListener? = null

    // The color of the progress ring
    private val arcPaint: Paint = Paint()
        .apply {
            color = DEFAULT_CIRCLE_PROGRESS_COLOR
            isAntiAlias = true
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }

    // The progress circle ring background
    private val circlePaint: Paint = Paint()
        .apply {
            color = DEFAULT_CIRCLE_COLOR
            isAntiAlias = true
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }

    private val thumb1: Thumb = Thumb(context) { x, y -> thumbTouch(true, x, y) }
    private val thumb2: Thumb = Thumb(context) { x, y -> thumbTouch(false, x, y) }
    private var thumbActive: Thumb? = null

    private var progress1 = 0
    private var progress2 = 0
    var startAngle by Delegates.observable(270.0) { _, old, new ->
        if (old != new) {
            setProgressInternal(progress1, progress2, false, true)
        }
    }
    private var angle1 = startAngle
    private var angle2 = startAngle

    var progressMax by uiProperty(100)

    private var size = -1
    private var thumbSize = -1
    private val arcRect = RectF() // The rectangle containing our circles and arcs

    var seekBarStyle = 0

    init {
        setBackgroundColor(Color.TRANSPARENT)
        setImageResource(R.drawable.scrubber_control_holo)
    }


    private val ripple: NonChangingBoundsRippleDrawable? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NonChangingBoundsRippleDrawable(
                ColorStateList(
                    arrayOf(intArrayOf()),
                    intArrayOf(Color.LTGRAY)
                ), null, null
            )
                .also { background = it }
        } else {
            null
        }

    fun setImageResource(@DrawableRes resId: Int) {
        thumb1.setImageResource(resId)
        thumb2.setImageResource(resId)
        thumbSize = Math.max(
            thumb1.drawable.let { Math.max(it.intrinsicWidth, it.intrinsicHeight) },
            thumb2.drawable.let { Math.max(it.intrinsicWidth, it.intrinsicHeight) }
        )
        updateRect()
        post { invalidate() }
    }

    fun setProgress(progress1: Int, progress2: Int) {
        setProgressInternal(progress1, progress2, false, false)
    }

    override fun onAttachedToWindow() {

        Log.d(TAG, "seekBarStyle onAttachedToWindow : $seekBarStyle")
        if (seekBarStyle == 1) {
//            thumb1.isClickable = false
//            thumb1.isActivated = false
//            thumb1.isSelectable(false)
            addView(thumb1)
        }

        addView(thumb2)
        super.onAttachedToWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width: Int =
            if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
                Math.max(widthSize, heightSize)
            } else {
                widthSize
            }

        val height: Int =
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
                Math.max(widthSize, heightSize)
            } else {
                heightSize
            }

        val newSize = Math.min(width, height)
        if (newSize != size) {
            size = newSize
            updateRect()
            invalidate()
        }
        val spec = MeasureSpec.makeMeasureSpec(newSize, MeasureSpec.EXACTLY)
        //MUST CALL THIS
        super.onMeasure(spec, spec)
    }

    override fun onDraw(canvas: Canvas) {
        val mid = size.toFloat() / 2
        val radius = mid - (thumbSize.toFloat() / 2)
        canvas.drawCircle(mid, mid, radius, circlePaint)
        canvas.drawArc(
            arcRect,
            angle1.toFloat(),
            (angle2 - angle1).inDegrees().toFloat(),
            false,
            arcPaint
        )

        setPadding(thumb1, angle1)
        setPadding(thumb2, angle2)

        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean =
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val mid = size.toFloat() / 2.0
                val innerR = mid - thumbSize
                val dx = event.x - mid
                val dy = event.y - mid
                val rSq = dx * dx + dy * dy
                Log.d(TAG, "r = ${Math.sqrt(rSq)}, outer = $mid, inner = $innerR")
                if (rSq < mid * mid && rSq > innerR * innerR) {
                    isPressed = true

                    if (seekBarStyle == 1) {
                        if (sqDist(event.x, event.y, thumb1) <= sqDist(event.x, event.y, thumb2)) {
                            thumb1
                        } else {
                            thumb2
                        }
                    } else {
                        thumb2
                    }
                        .also { thumbActive = it }
                        .internalOnTouchEvent(event)
                } else {
                    false
                }
            }
            MotionEvent.ACTION_MOVE ->
                thumbActive
                    ?.internalOnTouchEvent(event)
                    ?: false
            else -> {
                isPressed = false
                thumbActive
                    ?.also { thumbActive = null }
                    ?.internalOnTouchEvent(event)
                    ?: false
            }
        }

    private fun sqDist(x: Float, y: Float, thumb: Thumb): Float {
        val dx = thumb.paddingLeft + thumbSize / 2 - x
        val dy = thumb.paddingTop + thumbSize / 2 - y
        return dx * dx + dy * dy
    }

    private fun setPadding(thumb: Thumb, angle: Double) {
        val mid = (size - thumbSize) / 2
        val paddingLeft = mid + (Math.cos(Math.toRadians(angle)) * mid).toInt()
        val paddingTop = mid + (Math.sin(Math.toRadians(angle)) * mid).toInt()
        thumb.setPadding(paddingLeft, paddingTop, thumbSize)
    }

    private fun updateRect() {
        val upperLeft = thumbSize.toFloat() / 2
        val lowerRight = size.toFloat() - upperLeft
        arcRect.set(upperLeft, upperLeft, lowerRight, lowerRight)
    }


    private fun setProgressInternal(
        progress1: Int,
        progress2: Int,
        fromUser: Boolean,
        forceChange: Boolean
    ) {
        var changed = forceChange

        progress1
            .limitProgress()
            .takeUnless { it == this.progress1 }
            ?.let {
                this.progress1 = it
                changed = true
            }
        progress2
            .limitProgress()
            .takeUnless { it == this.progress2 }
            ?.let {
                this.progress2 = it
                changed = true
            }

        if (changed) {
            angle1 = (progress1.toDouble() * 360 / progressMax + startAngle).inDegrees()
            angle2 = (progress2.toDouble() * 360 / progressMax + startAngle).inDegrees()
            post {
                invalidate()
                seekBarChangeListener?.onProgressChange(this, progress1, progress2, fromUser)
            }
        }
    }

    private tailrec fun Int.limitProgress(): Int = when {
        this < 0 -> (this + progressMax).limitProgress()
        this >= progressMax -> (this - progressMax).limitProgress()
        else -> this
    }

    private tailrec fun Double.inDegrees(): Double = when {
        this < 0.0 -> (this + 360.0).inDegrees()
        this >= 360.0 -> (this - 360.0).inDegrees()
        else -> this
    }

    private fun thumbTouch(isThumb1: Boolean, xIn: Float, yIn: Float) {
        val halfSize = size.toDouble() / 2.0
        val x = xIn.toDouble() - halfSize
        val y = yIn.toDouble() - halfSize

        val angle =
            (360.0 / 2.0 / Math.PI *
                    if (0.0 == x) {
                        if (y > 0) Math.PI / 2
                        else -Math.PI / 2
                    } else {
                        Math.atan(y / x) + if (x >= 0) 0.0 else Math.PI
                    } -
                    startAngle)
                .inDegrees()

        val progress = (angle / 360.0 * progressMax + .5).toInt()

        if (isThumb1) {
            setProgressInternal(progress, progress2, true, false)
        } else {
            setProgressInternal(progress1, progress, true, false)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val overflow = thumbSize / 2
            val r = halfSize - thumbSize / 2
            val a = (progress.toDouble() * 360 / progressMax + startAngle).inDegrees()
            val activeX = (Math.cos(Math.toRadians(a)) * r) + halfSize
            val activeY = (Math.sin(Math.toRadians(a)) * r) + halfSize
            drawableHotspotChanged(activeX.toFloat(), activeY.toFloat())
            ripple?.setBoundsInternal(
                activeX.toInt() - overflow,
                activeY.toInt() - overflow,
                activeX.toInt() + overflow,
                activeY.toInt() + overflow
            )
        }
    }

    private fun <T> uiProperty(value: T) = Delegates.observable(value) { _, old, new ->
        if (old != new) post { invalidate() }
    }

    companion object {

        @Suppress("FunctionName")
        fun OnSeekChangeListener(listener: (CircularRangeSeekBar, Int, Int, Boolean) -> Unit): OnSeekChangeListener =
            object : OnSeekChangeListener {
                override fun onProgressChange(
                    view: CircularRangeSeekBar,
                    progress1: Int,
                    progress2: Int,
                    fromUser: Boolean
                ) =
                    listener(view, progress1, progress2, fromUser)
            }

    }

    interface OnSeekChangeListener {
        fun onProgressChange(
            view: CircularRangeSeekBar,
            progress1: Int,
            progress2: Int,
            fromUser: Boolean
        )
    }

    private class Thumb(context: Context, val updateLocation: (x: Float, y: Float) -> Unit) :
        ImageView(context) {

        init {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            isClickable = true
            isFocusable = true
        }

        fun isSelectable(isSelectable: Boolean) {
            isClickable = isSelectable
            isFocusable = isSelectable
        }

        internal fun internalOnTouchEvent(event: MotionEvent): Boolean =
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (null != parent)
                        parent.requestDisallowInterceptTouchEvent(true)
                    super.onTouchEvent(event)
                }
                MotionEvent.ACTION_MOVE -> {
                    updateLocation(event.x, event.y)
                    true
                }
                else ->
                    super.onTouchEvent(event)
            }

        fun setPadding(left: Int, top: Int, thumbSize: Int) {
            setPadding(left, top, 0, 0)
        }


        override fun onTouchEvent(event: MotionEvent): Boolean = false

    }
}


