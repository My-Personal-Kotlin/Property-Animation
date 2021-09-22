package com.propertyanimation

import android.animation.*
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById(R.id.rotateButton)
        translateButton = findViewById(R.id.translateButton)
        scaleButton = findViewById(R.id.scaleButton)
        fadeButton = findViewById(R.id.fadeButton)
        colorizeButton = findViewById(R.id.colorizeButton)
        showerButton = findViewById(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }

    }

    // Extension Function Created   becoz   Animator made from ObjectAnimator
    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        duration = 1000
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

    private fun rotater() {
        val animator : ObjectAnimator = ObjectAnimator.ofFloat(
            star, View.ROTATION,-360f,0f)
//        animator.duration = 1000    Because it is done in Extension Function
        animator.disableViewDuringAnimation(rotateButton) // Extension Function
        animator.start()
    }

    private fun translater() {
        val animator : ObjectAnimator = ObjectAnimator.ofFloat(
            star, View.TRANSLATION_X,250f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
//        animator.duration = 1000    Because it is done in Extension Function
        animator.disableViewDuringAnimation(translateButton) // Extension Function
        animator.start()
    }

    private fun scaler() {
        val ScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,4f)
        val ScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,4f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(star,ScaleX,ScaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()
    }

     // value range from [0 - 1] [invisible - visible]
    private fun fader() {
        val animator : ObjectAnimator = ObjectAnimator.ofFloat(
            star, View.ALPHA,0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(fadeButton) // Extension Function
        animator.start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun colorizer() {
        //
        // Creates Flashes while changing color from black to rea
        //val animator = ObjectAnimator.ofInt(star.parent,"backgroundColor", Color.BLACK, Color.RED)
        var animator =
            ObjectAnimator.ofArgb(star.parent,"backgroundColor", Color.BLACK, Color.RED)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(colorizeButton)
        animator.start()
    }

    private fun shower() {
        // Getting a reference of FrameLayout
        val container = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()

        // Adding a new Star in Framelayout
        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        container.addView(newStar)

        // Resizing a new Star
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX // cache a new star width
        starH *= newStar.scaleY // cache a new star width

        // For getting from TOP on any place
        newStar.translationX = Math.random().toFloat() *
                containerW - starW / 2

        // Vertically Move from
        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y,
            -starH, containerH + starH)
        mover.interpolator = AccelerateInterpolator(1f)
        // Rotation while Moving
        val rotator = ObjectAnimator.ofFloat(newStar, View.ROTATION,
            (Math.random() * 1080).toFloat())
        rotator.interpolator = LinearInterpolator()

        // Run BOTH animation at same time
        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 700).toLong()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })
        set.start()
    }

}