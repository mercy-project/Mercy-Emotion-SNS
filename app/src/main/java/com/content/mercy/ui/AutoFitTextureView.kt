package com.content.mercy.ui

import android.content.Context
import android.util.AttributeSet
import android.view.TextureView

/**
 * Created by rapsealk on 2019-11-02..
 */
class AutoFitTextureView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : TextureView(context, attrs,  defStyle) {

    private var ratioWidth: Int = 0
    private var ratioHeight: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (ratioWidth == 0 || ratioHeight == 0) {
            setMeasuredDimension(width, height)
        } else if (width < height * ratioWidth / ratioHeight) {
            setMeasuredDimension(width, width * ratioHeight / ratioWidth)
        } else {
            setMeasuredDimension(height * ratioWidth / ratioHeight, height)
        }
    }

    fun setAspectRatio(width: Int, height: Int) {
        if (width < 0 || height < 0) {
            throw IllegalArgumentException("Size cannot be negative.")
        }
        ratioWidth = width
        ratioHeight = height
        requestLayout()
    }
}