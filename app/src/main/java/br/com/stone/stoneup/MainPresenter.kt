package br.com.stone.poladroid.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import br.com.stone.poladroid.PrintConfig
import br.com.stone.poladroid.R
import br.com.stone.poladroid.printer.NEXAdapter
import br.com.stone.poladroid.printer.PrinterAdapter
import br.com.stone.poladroid.resize
import br.com.stone.poladroid.sierraLite
import net.glxn.qrgen.android.QRCode
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast


/**
 * @author filpgame
 * @since 2017-07-10
 */
class MainPresenter(private val view: MainContract.View, private val printer: PrinterAdapter = NEXAdapter()) : AnkoLogger, MainContract.Presenter, PrinterAdapter by printer {

    override fun printPicture(bitmap: Bitmap) {
        doAsync {
            val status = printer.print {
                /* Stone Logo */
                leftIndent(15)
                val stoneLogo = BitmapFactory.decodeResource(view.viewContext.resources, R.drawable.stone_logo_black)
                printBitmap(stoneLogo)

                /* Picture */
                step(10)
                leftIndent(10)
                val resizedBitmap = bitmap.resize(view.imageWidth, view.imageHeight).sierraLite()
                printBitmap(resizedBitmap)

                /* Title */
                step(10)
//              fontSet(EFontTypeAscii.FONT_32_48, EFontTypeExtCode.FONT_16_32)
                leftIndent(PrintConfig.titleLeftIndent)
                printString(PrintConfig.title)

                /* QR Code */
                step(10)
                leftIndent(100)
                val qrCode = QRCode.from(PrintConfig.qrCodeLink).withSize(200, 200).bitmap()
                printBitmap(qrCode)

                /* QR Code Description */
                step(15)
//                fontSet(EFontTypeAscii.FONT_16_24, EFontTypeExtCode.FONT_16_16)
                leftIndent(PrintConfig.qrCodeDescLeftIndent)
                printString(PrintConfig.qrCodeDesc)

                /* Link to Display */
                step(10)
//                fontSet(EFontTypeAscii.FONT_16_32, EFontTypeExtCode.FONT_16_32)
                leftIndent(PrintConfig.linkToDisplayLeftIndent)
                printString(PrintConfig.linkToDisplay)

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