package br.com.stone.stoneup

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import br.com.stone.stoneup.picture.PaymentFlowListenerStub
import br.com.stone.stoneup.picture.resize
import br.com.stone.stoneup.picture.sierraLite
import br.com.stone.stoneup.printer.IngenicoAdapter
import br.com.stone.stoneup.printer.PosAdapter
import org.jetbrains.anko.*


/**
 * @author filpgame
 * @since 2017-07-10
 */
class MainPresenter(
    private val view: MainContract.View,
    private val pos: PosAdapter = IngenicoAdapter(view.viewContext)
) : AnkoLogger, MainContract.Presenter, PaymentFlowListenerStub(), PosAdapter by pos {

    override fun printPicture(bitmap: Bitmap) {
        doAsync {
            pos.print {
                step(10)
                leftIndent(10)

                if (stoneUpWithBG == null) {
                    val stoneUpLogo = BitmapFactory.decodeResource(
                        view.viewContext.resources, R.drawable.ic_logo_festa
                    ).resize(375, 78)
                    stoneUpWithBG = Bitmap.createBitmap(
                        stoneUpLogo.width,
                        stoneUpLogo.height,
                        stoneUpLogo.config
                    )  // Create another image the same size
                    stoneUpWithBG?.eraseColor(Color.WHITE)  // set its background to white, or whatever color you want
                    val canvas = Canvas(stoneUpWithBG!!)  // create a canvas to draw on the new image
                    canvas.drawBitmap(stoneUpLogo, 0f, 0f, null) // draw old image on the background
                    stoneUpLogo.recycle()  // clear out old image
                }

                printBitmap(stoneUpWithBG!!)

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
            pos.print {
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

    override fun onCameraResult(bitmap: Bitmap) {
        printPicture(bitmap)
        view.viewContext.runOnUiThread {
            view.hideIntro()
            view.showPrinting()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            view.hidePrinting()
            view.showIntro()
        }, 3000)
    }

    override fun init() {
        pos.init()
    }

    override fun startCardDetection() {
        Log.d("MainPresenter", "startCardDetection() called")
        pos.startCardReader {
            Log.d("MainPresenter", "startCardReader() callback called")
            doWhenCardInserted()
        }
    }

    private fun doWhenCardInserted() {
        Log.d("MainPresenter", "doWhenCardInserted() called")
        printSlip()
        view.viewContext.runOnUiThread {
            view.hideIntro()
            view.showWelcome()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            view.hideWelcome()
            view.showIntro()
        }, 5000)
        while (pos.hasCardInserted()) {
            Log.d("MainPresenter", "pos.hasCardInserted() = true")
        }
        Log.d("MainPresenter", "pos.hasCardInserted() = ${pos.hasCardInserted()}")
        startCardDetection()
    }

    override fun printSlip() {
        doAsync {
            pos.print {
                step(20)
                val filipeta = BitmapFactory.decodeResource(
                    view.viewContext.resources, R.drawable.filipeta
                ).resize(375, 723)
                printBitmap(filipeta)
                step(150)
            }
        }
    }


    companion object {
        var stoneUpWithBG: Bitmap? = null
    }
}
