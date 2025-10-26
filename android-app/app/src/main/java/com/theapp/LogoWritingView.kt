package com.theapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class LogoWritingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.parseColor("#D2691E") // Golden brown like in screenshots
        strokeWidth = 6f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
    }

    private val scriptPath = Path()
    private val animatedPath = Path()
    private val pathMeasure = PathMeasure()
    private var animationProgress = 0f

    init {
        createScriptPath()
        startWritingAnimation()
    }

    private fun createScriptPath() {
        scriptPath.reset()
        
        val centerX = 400f
        val centerY = 300f
        
        // Create the elegant script "the app" path exactly like the screenshots
        
        // Capital "T" with flourish
        scriptPath.moveTo(centerX - 200f, centerY - 80f)
        scriptPath.quadTo(centerX - 150f, centerY - 120f, centerX - 100f, centerY - 80f)
        scriptPath.quadTo(centerX - 80f, centerY - 60f, centerX - 90f, centerY - 40f)
        scriptPath.quadTo(centerX - 100f, centerY - 20f, centerX - 110f, centerY)
        scriptPath.quadTo(centerX - 120f, centerY + 20f, centerX - 130f, centerY + 40f)
        
        // Connect to "h"
        scriptPath.quadTo(centerX - 120f, centerY + 50f, centerX - 100f, centerY + 45f)
        scriptPath.quadTo(centerX - 80f, centerY + 40f, centerX - 70f, centerY + 20f)
        scriptPath.quadTo(centerX - 60f, centerY, centerX - 50f, centerY - 20f)
        scriptPath.quadTo(centerX - 40f, centerY - 40f, centerX - 30f, centerY - 20f)
        scriptPath.quadTo(centerX - 20f, centerY, centerX - 10f, centerY + 20f)
        scriptPath.quadTo(centerX, centerY + 40f, centerX + 10f, centerY + 30f)
        
        // Connect to "e"
        scriptPath.quadTo(centerX + 20f, centerY + 20f, centerX + 40f, centerY + 10f)
        scriptPath.quadTo(centerX + 60f, centerY, centerX + 70f, centerY - 10f)
        scriptPath.quadTo(centerX + 80f, centerY - 20f, centerX + 70f, centerY - 30f)
        scriptPath.quadTo(centerX + 60f, centerY - 40f, centerX + 40f, centerY - 35f)
        scriptPath.quadTo(centerX + 20f, centerY - 30f, centerX + 30f, centerY - 15f)
        scriptPath.quadTo(centerX + 40f, centerY, centerX + 50f, centerY + 15f)
        
        // Space and "a"
        scriptPath.moveTo(centerX + 100f, centerY + 10f)
        scriptPath.quadTo(centerX + 120f, centerY - 10f, centerX + 150f, centerY)
        scriptPath.quadTo(centerX + 180f, centerY + 10f, centerX + 170f, centerY + 30f)
        scriptPath.quadTo(centerX + 160f, centerY + 50f, centerX + 140f, centerY + 45f)
        scriptPath.quadTo(centerX + 120f, centerY + 40f, centerX + 130f, centerY + 25f)
        scriptPath.lineTo(centerX + 150f, centerY + 35f)
        
        // First "p"
        scriptPath.quadTo(centerX + 160f, centerY + 40f, centerX + 180f, centerY + 35f)
        scriptPath.quadTo(centerX + 200f, centerY + 30f, centerX + 190f, centerY + 10f)
        scriptPath.quadTo(centerX + 180f, centerY - 10f, centerX + 170f, centerY + 10f)
        scriptPath.quadTo(centerX + 160f, centerY + 30f, centerX + 170f, centerY + 50f)
        scriptPath.quadTo(centerX + 180f, centerY + 70f, centerX + 190f, centerY + 90f)
        
        // Second "p"
        scriptPath.quadTo(centerX + 200f, centerY + 100f, centerX + 220f, centerY + 90f)
        scriptPath.quadTo(centerX + 240f, centerY + 80f, centerX + 230f, centerY + 60f)
        scriptPath.quadTo(centerX + 220f, centerY + 40f, centerX + 210f, centerY + 20f)
        scriptPath.quadTo(centerX + 200f, centerY, centerX + 220f, centerY + 10f)
        scriptPath.quadTo(centerX + 240f, centerY + 20f, centerX + 250f, centerY + 40f)
        scriptPath.quadTo(centerX + 260f, centerY + 60f, centerX + 240f, centerY + 70f)
        
        // Final elegant flourish
        scriptPath.quadTo(centerX + 220f, centerY + 80f, centerX + 260f, centerY + 85f)
        scriptPath.quadTo(centerX + 300f, centerY + 90f, centerX + 320f, centerY + 70f)
        scriptPath.quadTo(centerX + 340f, centerY + 50f, centerX + 320f, centerY + 30f)
        scriptPath.quadTo(centerX + 300f, centerY + 10f, centerX + 280f, centerY + 25f)
        scriptPath.quadTo(centerX + 260f, centerY + 40f, centerX + 270f, centerY + 60f)
        
        pathMeasure.setPath(scriptPath, false)
    }

    private fun startWritingAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 3000 // 3 seconds like in the screenshots
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                animationProgress = animation.animatedValue as Float
                invalidate()
            }
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        animatedPath.reset()
        
        val pathLength = pathMeasure.length
        val drawLength = pathLength * animationProgress
        
        pathMeasure.getSegment(0f, drawLength, animatedPath, true)
        
        // Draw with subtle glow effect like in screenshots
        val glowPaint = Paint(paint).apply {
            strokeWidth = 10f
            alpha = 80
            maskFilter = BlurMaskFilter(3f, BlurMaskFilter.Blur.NORMAL)
        }
        canvas.drawPath(animatedPath, glowPaint)
        canvas.drawPath(animatedPath, paint)
    }
}
