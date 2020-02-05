package br.com.stone.stoneup.printer

import android.graphics.Bitmap

/**
 * @author filpgame
 * @since 2017-08-07
 */
interface PosAdapter {
    fun leftIndent(count: Int)
    fun printBitmap(bitmap: Bitmap)
    fun printString(string: String)
    fun fontSet()
    fun initPrinter()
    fun startPrinting()
    fun step(count: Int)
    fun hasCardInserted(): Boolean
    fun print(toPrint: PosAdapter.() -> Unit) {
        initPrinter()
        this.apply(toPrint)
        startPrinting()
    }

    fun init()
    fun startCardReader(onCardDetected: () -> Unit)
}