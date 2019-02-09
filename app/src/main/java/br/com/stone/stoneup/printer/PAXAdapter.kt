package br.com.stone.poladroid.printer

import android.content.Context
import android.graphics.Bitmap
import com.pax.dal.IPrinter
import com.pax.neptunelite.api.NeptuneLiteUser

/**
 * @author filpgame
 * @since 2017-08-07
 */
class PAXAdapter(context: Context) : PrinterAdapter {
    val printer: IPrinter by lazy { NeptuneLiteUser.getInstance().getDal(context).printer }

    override fun step(count: Int) {
        printer.step(count)
    }

    override fun initPrinter() {
        printer.init()
    }

    override fun startPrinting() {
        printer.start()
    }

    override fun leftIndent(count: Int) {
        printer.leftIndent(count)
    }

    override fun printBitmap(bitmap: Bitmap) {
        printer.printBitmap(bitmap)
    }

    override fun printString(string: String) {
        printer.printStr(string, null)
    }

    override fun fontSet() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}