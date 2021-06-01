package ru.bedsus.imageswitcher

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageSwitcher
import android.widget.ImageView
import androidx.annotation.AnimRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class AutoImageSwitcher @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ImageSwitcher(context, attrs) {

    private val imageList = mutableListOf<String>()
    private val drawableSet = mutableMapOf<String, Drawable?>()
    private val drawableList: List<Drawable>
        get() = drawableSet.values.filterNotNull()

    private var currentImageIndex = 0

    @AnimRes
    private val inAnimationRes: Int
    @AnimRes
    private val outAnimationRes: Int
    private val showMilliseconds: Long
    private val placeholder: Drawable?

    init {
        setFactory {
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            }
        }

        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.AutoImageSwitcher,
                0,
                0
        ).apply {
            try {
                inAnimationRes = getInteger(
                        R.styleable.AutoImageSwitcher_inAnimation,
                        DEFAULT_IN_ANIMATION_RES
                )
                outAnimationRes = getInteger(
                        R.styleable.AutoImageSwitcher_inAnimation,
                        DEFAULT_OUT_ANIMATION_RES
                )
                showMilliseconds = getInteger(
                        R.styleable.AutoImageSwitcher_showMilliseconds,
                        DEFAULT_SHOWING_MILLISECONDS
                ).toLong()
                placeholder = getDrawable(R.styleable.AutoImageSwitcher_placeholder)
            } finally {
                recycle()
            }
        }

        inAnimation = AnimationUtils.loadAnimation(context, inAnimationRes)
        outAnimation = AnimationUtils.loadAnimation(context, outAnimationRes)
    }

    private val timer = object : CountDownTimer(showMilliseconds, showMilliseconds) {
        override fun onTick(p0: Long) { }
        override fun onFinish() {
            currentImageIndex++
            if (currentImageIndex == imageList.size) {
                currentImageIndex = 0
            }
            setImageDrawable(drawableList[currentImageIndex])
            Log.d(TAG, "show load image ${imageList[currentImageIndex]}")
            start()
        }
    }

    fun showImages(imagesUrl: List<String>) {
        clean()
        if (imagesUrl.isNotEmpty()) {
            imageList.addAll(imagesUrl)
            initialLoadImage()
        }
    }

    private fun initialLoadImage() {
        imageList.forEach { imageUrl ->
            loadImage(imageUrl) { drawable ->
                drawableSet[imageUrl] = drawable
                if (drawableSet.size == imageList.size && drawableList.isNotEmpty()) {
                    setImageDrawable(drawableList[0])
                    if (drawableList.size > 1) {
                        timer.start()
                    }
                }
            }
        }
    }

    private fun loadImage(imageUrl: String, isFinish: (resource: Drawable?) -> Unit = { }) {
        Glide.with(context)
                .load(imageUrl)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        Log.d(TAG, "Glide error load image $imageUrl")
                        isFinish(errorDrawable)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        Log.d(TAG, "Glide onLoadCleared image $imageUrl")
                    }

                    override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable?>?
                    ) {
                        Log.d(TAG, "Glide load image $imageUrl")
                        isFinish(resource)
                    }
                })
    }

    private fun clean() {
        Log.d(TAG, "clean data")
        currentImageIndex = 0
        timer.cancel()
        drawableSet.clear()
        imageList.clear()
    }

    companion object {
        const val TAG = "AutoImageSwitcher"
        @AnimRes
        const val DEFAULT_IN_ANIMATION_RES = android.R.anim.fade_in
        @AnimRes
        const val DEFAULT_OUT_ANIMATION_RES = android.R.anim.fade_out
        const val DEFAULT_SHOWING_MILLISECONDS = 1500
    }
}