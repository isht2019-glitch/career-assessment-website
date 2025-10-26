package com.theapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class AnimatedScriptView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.parseColor("#D2691E") // Golden brown matching video
        strokeWidth = 8f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
    }

    private val completePath = Path()
    private val animatedPath = Path()
    private val pathMeasure = PathMeasure()
    private var animationProgress = 0f

    init {
        createCompleteScriptPath()
        startAnimation()
    }

    private fun createCompleteScriptPath() {
        completePath.reset()
        
        // Create the complete "the app" script as one continuous path
        // Starting with elegant capital "T" with flourish
        completePath.moveTo(80f, 60f)
        completePath.quadTo(120f, 40f, 160f, 60f)
        completePath.quadTo(180f, 70f, 170f, 90f)
        completePath.quadTo(160f, 110f, 150f, 130f)
        
        // Connect to "h"
        completePath.quadTo(140f, 140f, 160f, 145f)
        completePath.quadTo(180f, 150f, 200f, 140f)
        completePath.quadTo(220f, 130f, 210f, 110f)
        completePath.quadTo(200f, 90f, 220f, 100f)
        completePath.quadTo(240f, 110f, 250f, 130f)
        
        // Connect to "e"
        completePath.quadTo(260f, 140f, 280f, 135f)
        completePath.quadTo(300f, 130f, 310f, 115f)
        completePath.quadTo(320f, 100f, 300f, 95f)
        completePath.quadTo(280f, 90f, 270f, 105f)
        completePath.quadTo(260f, 120f, 280f, 125f)
        
        // Space and connect to "a"
        completePath.moveTo(350f, 120f)
        completePath.quadTo(370f, 100f, 400f, 110f)
        completePath.quadTo(430f, 120f, 420f, 140f)
        completePath.quadTo(410f, 160f, 390f, 155f)
        completePath.quadTo(370f, 150f, 380f, 135f)
        completePath.lineTo(400f, 145f)
        
        // Connect to first "p"
        completePath.quadTo(420f, 150f, 440f, 140f)
        completePath.quadTo(460f, 130f, 450f, 110f)
        completePath.quadTo(440f, 90f, 430f, 110f)
        completePath.quadTo(420f, 130f, 430f, 150f)
        completePath.quadTo(440f, 170f, 450f, 190f)
        
        // Connect to second "p"
        completePath.quadTo(460f, 200f, 480f, 190f)
        completePath.quadTo(500f, 180f, 490f, 160f)
        completePath.quadTo(480f, 140f, 470f, 120f)
        completePath.quadTo(460f, 100f, 480f, 110f)
        completePath.quadTo(500f, 120f, 510f, 140f)
        completePath.quadTo(520f, 160f, 500f, 170f)
        
        // Final flourish
        completePath.quadTo(480f, 180f, 520f, 185f)
        completePath.quadTo(560f, 190f, 580f, 170f)
        completePath.quadTo(600f, 150f, 580f, 130f)
        completePath.quadTo(560f, 110f, 540f, 125f)
        completePath.quadTo(520f, 140f, 530f, 160f)
        
        pathMeasure.setPath(completePath, false)
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 3500 // 3.5 seconds like the video
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
        
        // Draw with slight glow effect
        val glowPaint = Paint(paint).apply {
            strokeWidth = 12f
            alpha = 60
            maskFilter = BlurMaskFilter(4f, BlurMaskFilter.Blur.NORMAL)
        }
        canvas.drawPath(animatedPath, glowPaint)
        canvas.drawPath(animatedPath, paint)
    }
}
