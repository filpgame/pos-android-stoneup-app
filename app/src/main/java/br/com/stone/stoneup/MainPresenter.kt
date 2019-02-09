package br.com.stone.stoneup

import android.graphics.Bitmap
import android.os.ConditionVariable
import android.util.Log
import br.com.stone.payment.domain.datamodel.CandidateAppInfo
import br.com.stone.payment.domain.datamodel.PaymentInfo
import br.com.stone.payment.domain.datamodel.Result
import br.com.stone.payment.domain.datamodel.TransAppSelectedInfo
import br.com.stone.payment.domain.interfaces.PaymentFlowCallback
import br.com.stone.payment.domain.interfaces.PaymentFlowListener
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
class MainPresenter(
    private val view: MainContract.View,
    private val printer: PrinterAdapter = IngenicoAdapter(view.viewContext)
) : AnkoLogger, MainContract.Presenter, PaymentFlowListener, PrinterAdapter by printer {

    private val conditionVariable = ConditionVariable()

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

    override fun printPictures(top: Bitmap, picture: Bitmap, bottom: Bitmap) {
        doAsync {
            val status = printer.print {
                /* Picture */
                step(10)
                leftIndent(10)
                printBitmap(top)
                step(10)
                leftIndent(10)
                val resizedBitmap = picture.resize(view.imageWidth, view.imageHeight).sierraLite()
                printBitmap(resizedBitmap)
                step(10)
                leftIndent(10)
                printBitmap(bottom)
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

    override fun onCameraResult() {
        Thread.sleep(5000)
        conditionVariable.open()
    }

    override fun startCardDetection() {
        PalHelper.startCardDetection(this)
    }

    override fun onCardDetection() {
        Log.d(this.javaClass.simpleName, "waiting card detection")
    }

    override fun onCardDetected(p0: Boolean) {
        Log.d(this.javaClass.simpleName, "card detected")
    }

    override fun onOnlineProcessing(p0: PaymentInfo?) {

    }

    override fun onTransAppSelection(p0: MutableList<CandidateAppInfo>?) {

    }

    override fun onCvvInsertion() {

    }

    override fun onInsertPassword(p0: Boolean, p1: Int, p2: Boolean) {
    }

    override fun onTransAppSelected(p0: TransAppSelectedInfo?) {
    }

    override fun onCallbackInstance(p0: PaymentFlowCallback?) {
    }

    override fun onCardReTap() {
    }

    override fun onResult(p0: Result?) {
        Log.d(this.javaClass.simpleName, "${p0?.message}")
        view.startCamera()
//        object : Thread() {
////            override fun run() {
////                onCameraResult()
////            }
////        }.start()
        conditionVariable.block()
    }

    override fun onInstallmentSelection() {
    }

    override fun onCardRemoved() {
        Log.d(this.javaClass.simpleName, "on card removed")
        startCardDetection()
    }

    override fun onRemoveCard() {
        Log.d(this.javaClass.simpleName, "on remove card")
    }

}
