package com.theapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class WritingAnimationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var logoImage: Bitmap? = null
    private val revealPaint = Paint().apply {
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }
    private val maskPath = Path()
    private val pathMeasure = PathMeasure()
    private var animationProgress = 0f
    private var maskBitmap: Bitmap? = null
    private var maskCanvas: Canvas? = null

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        createWritingPath()
        loadLogo()
        startAnimation()
    }

    private fun loadLogo() {
        try {
            val drawable = context.getDrawable(R.drawable.the_app_logo)
            drawable?.let {
                logoImage = Bitmap.createBitmap(
                    it.intrinsicWidth,
                    it.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(logoImage!!)
                it.setBounds(0, 0, canvas.width, canvas.height)
                it.draw(canvas)
            }
        } catch (e: Exception) {
            // Fallback if logo not found
        }
    }

    private fun createWritingPath() {
        maskPath.reset()
        
        // Create a path that follows the script writing pattern
        // This simulates the pen stroke order for "the app"
        
        // Start with "t" in "the"
        maskPath.moveTo(100f, 80f)
        maskPath.quadTo(120f, 60f, 140f, 80f)
        maskPath.quadTo(150f, 100f, 145f, 120f)
        maskPath.quadTo(140f, 140f, 135f, 160f)
        
        // Cross for "t"
        maskPath.moveTo(90f, 100f)
        maskPath.lineTo(150f, 100f)
        
        // "h" in "the"
        maskPath.moveTo(170f, 70f)
        maskPath.quadTo(175f, 90f, 180f, 110f)
        maskPath.quadTo(185f, 130f, 190f, 150f)
        maskPath.moveTo(180f, 110f)
        maskPath.quadTo(195f, 100f, 210f, 110f)
        maskPath.quadTo(220f, 120f, 215f, 140f)
        
        // "e" in "the"
        maskPath.moveTo(240f, 120f)
        maskPath.quadTo(260f, 110f, 275f, 120f)
        maskPath.quadTo(285f, 130f, 280f, 140f)
        maskPath.quadTo(275f, 150f, 260f, 150f)
        maskPath.quadTo(245f, 150f, 240f, 140f)
        
        // Space and "a" in "app"
        maskPath.moveTo(320f, 130f)
        maskPath.quadTo(340f, 115f, 360f, 125f)
        maskPath.quadTo(375f, 135f, 370f, 150f)
        maskPath.quadTo(365f, 165f, 350f, 160f)
        maskPath.moveTo(360f, 130f)
        maskPath.lineTo(360f, 160f)
        
        // First "p"
        maskPath.moveTo(390f, 110f)
        maskPath.lineTo(390f, 180f)
        maskPath.moveTo(390f, 130f)
        maskPath.quadTo(405f, 120f, 420f, 130f)
        maskPath.quadTo(430f, 140f, 425f, 150f)
        maskPath.quadTo(420f, 160f, 405f, 160f)
        maskPath.lineTo(390f, 160f)
        
        // Second "p"
        maskPath.moveTo(450f, 110f)
        maskPath.lineTo(450f, 180f)
        maskPath.moveTo(450f, 130f)
        maskPath.quadTo(465f, 120f, 480f, 130f)
        maskPath.quadTo(490f, 140f, 485f, 150f)
        maskPath.quadTo(480f, 160f, 465f, 160f)
        maskPath.lineTo(450f, 160f)
        
        // Final flourish
        maskPath.moveTo(500f, 140f)
        maskPath.quadTo(520f, 130f, 540f, 145f)
        maskPath.quadTo(560f, 160f, 545f, 175f)
        maskPath.quadTo(530f, 190f, 515f, 180f)
        
        pathMeasure.setPath(maskPath, false)
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 3000
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                animationProgress = animation.animatedValue as Float
                invalidate()
            }
        }
        animator.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        maskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        maskCanvas = Canvas(maskBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        logoImage?.let { logo ->
            maskBitmap?.let { mask ->
                maskCanvas?.let { mCanvas ->
                    
                    // Clear the mask
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                    
                    // Draw the revealed portion of the path
                    val pathLength = pathMeasure.length
                    val drawLength = pathLength * animationProgress
                    
                    val revealPath = Path()
                    pathMeasure.getSegment(0f, drawLength, revealPath, true)
                    
                    val strokePaint = Paint().apply {
                        color = Color.WHITE
                        strokeWidth = 20f
                        style = Paint.Style.STROKE
                        strokeCap = Paint.Cap.ROUND
                        strokeJoin = Paint.Join.ROUND
                        isAntiAlias = true
                    }
                    
                    mCanvas.drawPath(revealPath, strokePaint)
                    
                    // Scale and center the logo
                    val scale = minOf(width.toFloat() / logo.width, height.toFloat() / logo.height) * 0.8f
                    val scaledWidth = logo.width * scale
                    val scaledHeight = logo.height * scale
                    val left = (width - scaledWidth) / 2
                    val top = (height - scaledHeight) / 2
                    
                    val destRect = RectF(left, top, left + scaledWidth, top + scaledHeight)
                    
                    // Draw the logo
                    canvas.drawBitmap(logo, null, destRect, null)
                    
                    // Apply the mask to reveal only the written portion
                    canvas.drawBitmap(mask, 0f, 0f, revealPaint)
                }
            }
        }
    }
}
