package br.com.stone.poladroid

import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.View
import android.view.animation.Animation
import com.pax.dal.IPrinter


fun Bitmap.resize(newWidth: Int, newHeight: Int): Bitmap {
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // CREATE A MATRIX FOR THE MANIPULATION
    val matrix = Matrix()
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight)

    // "RECREATE" THE NEW BITMAP
    //    this.recycle()
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

fun IPrinter.print(toPrint: IPrinter.() -> Unit): Int {
    init()
    this.apply(toPrint)
    return start()
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun Animation.animationListener(listener: KAnimationListener.() -> Unit) {
    setAnimationListener(KAnimationListener().apply(listener))
}

class KAnimationListener : Animation.AnimationListener {
    private var onAnimationRepeat: ((Animation?) -> Unit)? = null
    private var onAnimationStart: ((Animation?) -> Unit)? = null
    private var onAnimationEnd: ((Animation?) -> Unit)? = null

    override fun onAnimationRepeat(animation: Animation?) {
        onAnimationRepeat?.invoke(animation)
    }

    override fun onAnimationStart(animation: Animation?) {
        onAnimationStart?.invoke(animation)
    }

    override fun onAnimationEnd(animation: Animation?) {
        onAnimationEnd?.invoke(animation)
    }

    fun start(func: ((Animation?) -> Unit)?) {
        onAnimationStart = func
    }

    fun repeat(func: ((Animation?) -> Unit)?) {
        onAnimationRepeat = func
    }

    fun end(func: ((Animation?) -> Unit)?) {
        onAnimationEnd = func
    }
}