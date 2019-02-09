package br.com.stone.poladroid.printer

import android.graphics.Bitmap

/**
 * @author filpgame
 * @since 2017-08-07
 */
interface PrinterAdapter {
    fun leftIndent(count: Int)
    fun printBitmap(bitmap: Bitmap)
    fun printString(string: String)
    fun fontSet()
    fun initPrinter()
    fun startPrinting()
    fun step(count: Int)
    fun print(toPrint: PrinterAdapter.() -> Unit) {
        initPrinter()
        this.apply(toPrint)
        startPrinting()
    }
}