package br.com.stone.stoneup.picture

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.view.View
import android.view.animation.Animation
import br.com.stone.payment.domain.datamodel.CandidateAppInfo
import br.com.stone.payment.domain.datamodel.PaymentInfo
import br.com.stone.payment.domain.datamodel.Result
import br.com.stone.payment.domain.datamodel.TransAppSelectedInfo
import br.com.stone.payment.domain.interfaces.PaymentFlowCallback
import br.com.stone.payment.domain.interfaces.PaymentFlowListener
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

open class PaymentFlowListenerStub : PaymentFlowListener {

    override fun onOnlineProcessing(p0: PaymentInfo?) {
        Log.d("Extensions", "onOnlineProcessing() called with: p0 = [${p0}]")
    }

    override fun onCardDetected(p0: Boolean) {
        Log.d("Extensions", "onCardDetected() called with: p0 = [${p0}]")
    }

    override fun onCardDetection() {
        Log.d("Extensions", "onCardDetection() called")
    }

    override fun onTransAppSelection(p0: MutableList<CandidateAppInfo>?) {
        Log.d("Extensions", "onTransAppSelection() called with: p0 = [${p0}]")
    }

    override fun onCvvInsertion() {
        Log.d("Extensions", "onCvvInsertion() called")
    }

    override fun onInsertPassword(p0: Boolean, p1: Int, p2: Boolean) {
        Log.d("Extensions", "onInsertPassword() called with: p0 = [${p0}], p1 = [${p1}], p2 = [${p2}]")
    }

    override fun onTransAppSelected(p0: TransAppSelectedInfo?) {
        Log.d("Extensions", "onTransAppSelected() called with: p0 = [${p0}]")
    }

    override fun onRemoveCard() {
        Log.d("Extensions", "onRemoveCard() called")
    }

    override fun onCallbackInstance(p0: PaymentFlowCallback?) {
        Log.d("Extensions", "onCallbackInstance() called with: p0 = [${p0}]")
    }

    override fun onCardReTap() {
        Log.d("Extensions", "onCardReTap() called")
    }

    override fun onResult(p0: Result?) {
        Log.d("Extensions", "onResult() called with: p0 = [${p0}]")
    }

    override fun onInstallmentSelection() {
        Log.d("Extensions", "onInstallmentSelection() called")
    }

    override fun onCardRemoved() {
        Log.d("Extensions", "onCardRemoved() called")
    }

}