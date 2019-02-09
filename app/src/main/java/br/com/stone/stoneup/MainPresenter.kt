package br.com.stone.stoneup

import android.graphics.Bitmap
import br.com.stone.poladroid.main.MainContract
import br.com.stone.poladroid.printer.IngenicoAdapter
import br.com.stone.poladroid.printer.PrinterAdapter
import br.com.stone.poladroid.resize
import br.com.stone.poladroid.sierraLite
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast


/**
 * @author filpgame
 * @since 2017-07-10
 */
class MainPresenter(private val view: MainContract.View, private val printer: PrinterAdapter = IngenicoAdapter(view.viewContext)) : AnkoLogger,
    MainContract.Presenter, PrinterAdapter by printer {

    override fun printPicture(bitmap: Bitmap) {
        doAsync {
            val status = printer.print {
                /* Picture */
                step(10)
                leftIndent(10)
                val resizedBitmap = bitmap.resize(view.imageWidth, view.imageHeight).sierraLite()
                printBitmap(resizedBitmap)
                step(150)
            }
        }
    }

    fun logPrintStatus(code: Int) {
        val status = when (code) {
            0 -> "Success"
            1 -> "Printer is busy"
            2 -> "Out of paper"
            3 -> "The format of print data packet error"
            4 -> "Printer malfunctions"
            8 -> "Printer over heats"
            9 -> "Printer voltage is too low"
            240 -> "Printing is unfinished"
            252 -> " The printer has not installed font library"
            254 -> "Data package is too long"
            else -> "Erro desconhecido"
        }

        debug("[PRINT STATUS] $status")
        view.viewContext.toast("[PRINT STATUS] $status")
    }
}