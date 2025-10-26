package com.theapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.math.*

class LogoRevealView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var animationProgress = 0f
    private var logoAlpha = 0f
    
    private val scriptPaint = Paint().apply {
        color = Color.parseColor("#D4A574")
        textSize = 80f
        typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private data class LetterState(
        var x: Float = 0f,
        var y: Float = 0f,
        var alpha: Float = 1f,
        var scale: Float = 1f
    )

    private val letters = mutableListOf<LetterState>()
    private val letterChars = listOf('t', 'h', 'e', ' ', 'a', 'p', 'p')
    private val finalPositions = listOf(-120f, -80f, -40f, 0f, 40f, 80f, 120f)

    init {
        // Initialize letters at their final positions
        repeat(letterChars.size) {
            letters.add(LetterState())
        }
        startAnimation()
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 3000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                animationProgress = animation.animatedValue as Float
                updateAnimation()
                invalidate()
            }
        }
        animator.start()
    }

    private fun updateAnimation() {
        when {
            // Phase 1: Logo appears (0.0 - 0.4)
            animationProgress <= 0.4f -> {
                logoAlpha = animationProgress / 0.4f
                letters.forEach { letter ->
                    letter.alpha = logoAlpha
                    letter.scale = 1f
                }
            }
            
            // Phase 2: Logo holds (0.4 - 0.6)
            animationProgress <= 0.6f -> {
                logoAlpha = 1f
                letters.forEach { letter ->
                    letter.alpha = 1f
                    letter.scale = 1f
                }
            }
            
            // Phase 3: Letters separate and disappear (0.6 - 1.0)
            else -> {
                val separateProgress = (animationProgress - 0.6f) / 0.4f
                
                letters.forEachIndexed { index, letter ->
                    if (letterChars[index] != ' ') {
                        // Calculate movement direction for each letter
                        val angle = when(index) {
                            0 -> -120f  // t
                            1 -> -60f   // h  
                            2 -> 0f     // e
                            4 -> 60f    // a
                            5 -> 120f   // p
                            6 -> 180f   // p
                            else -> 0f
                        }
                        
                        val distance = separateProgress * 200f
                        letter.x = cos(Math.toRadians(angle.toDouble())).toFloat() * distance
                        letter.y = sin(Math.toRadians(angle.toDouble())).toFloat() * distance
                        
                        // Fade out and scale up
                        letter.alpha = 1f - separateProgress
                        letter.scale = 1f + separateProgress * 0.5f
                    }
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        
        letters.forEachIndexed { index, letter ->
            val char = letterChars[index].toString()
            if (char != " " && letter.alpha > 0f) {
                canvas.save()
                
                val finalX = centerX + finalPositions[index] + letter.x
                val finalY = centerY + letter.y
                
                canvas.translate(finalX, finalY)
                canvas.scale(letter.scale, letter.scale)
                
                scriptPaint.alpha = (letter.alpha * 255).toInt()
                
                // Draw with glow
                val glowPaint = Paint(scriptPaint).apply {
                    strokeWidth = 3f
                    alpha = (letter.alpha * 100).toInt()
                    maskFilter = BlurMaskFilter(2f, BlurMaskFilter.Blur.NORMAL)
                    style = Paint.Style.STROKE
                }
                
                canvas.drawText(char, -20f, 20f, glowPaint)
                canvas.drawText(char, -20f, 20f, scriptPaint)
                
                canvas.restore()
            }
        }
    }
}
