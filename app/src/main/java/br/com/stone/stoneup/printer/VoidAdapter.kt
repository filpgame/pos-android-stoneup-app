package br.com.stone.stoneup.printer

import android.graphics.Bitmap
import android.os.Handler
import android.util.Log

/**
 * @author filpgame
 * @since 2017-08-07
 */
class VoidAdapter : PosAdapter {
    private val TAG = "VoidAdapter"

    override fun initPrinter() {
        Log.d(TAG, "initPrinter() called.")
    }

    override fun startPrinting() {
        Log.d(TAG, "startPrinting() called.")
    }

    override fun step(count: Int) {
        Log.d(TAG, "step() called. parameters: count = $count")
    }

    override fun hasCardInserted(): Boolean {
        Log.d("VoidAdapter", "hasCardInserted() called")
        return (System.currentTimeMillis() / 1000) % 2 == 0L
    }

    override fun init() {
        Log.d("VoidAdapter", "init() called")
    }

    override fun startCardReader(onCardDetected: () -> Unit) {
        Log.d(TAG, "startCardReader() called. parameters: onCardDetected = $onCardDetected")
        Handler().postDelayed({
            Log.d(TAG, "Invoking onCardDetected callback")
            onCardDetected.invoke()
        }, 1000)
    }

    override fun leftIndent(count: Int) {
        Log.d(TAG, "leftIndent() called. parameters: count = $count")
    }

    override fun printBitmap(bitmap: Bitmap) {
        Log.d(TAG, "printBitmap() called. parameters: bitmap = $bitmap")
    }

    override fun printString(string: String) {
        Log.d(TAG, "printString() called. parameters: string: $string")
    }

    override fun fontSet() {
        Log.d(TAG, "fontSet() called.")
    }
}