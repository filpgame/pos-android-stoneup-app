package br.com.stone.poladroid.printer

import android.graphics.Bitmap
import android.util.Log

/**
 * @author filpgame
 * @since 2017-08-07
 */
class VoidAdapter : PrinterAdapter {
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