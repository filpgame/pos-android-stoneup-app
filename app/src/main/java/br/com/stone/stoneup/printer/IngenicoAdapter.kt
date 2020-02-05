package br.com.stone.stoneup.printer

import android.content.Context
import android.graphics.Bitmap
import android.os.RemoteException
import android.util.Log
import br.com.stone.stoneup.printer.ingenico.IngenicoServiceClient
import br.com.stone.stoneup.printer.ingenico.IngenicoServiceClient.Companion.MAX_GRAY_SCALE
import com.usdk.apiservice.aidl.icreader.OnInsertListener
import com.usdk.apiservice.aidl.printer.*
import java.io.ByteArrayOutputStream

/**
 * @author filpgame
 * @since 2017-08-07
 */
class IngenicoAdapter(var context: Context) : PosAdapter {
    private val ingenicoService: IngenicoServiceClient by lazy { IngenicoServiceClient(context) }
    private val printer by lazy { ingenicoService.getPrinter() }
    private val icReader by lazy { ingenicoService.getIcReader() }

    override fun step(count: Int) {
        printer?.feedPix(count)
    }

    override fun hasCardInserted(): Boolean {
        return icReader?.isCardIn ?: false
    }

    override fun startCardReader(onCardDetected: () -> Unit) {
        if (ingenicoService.getDeviceService() == null)
            ingenicoService.bindSdkDeviceService()

        icReader?.searchCard(object : OnInsertListener.Stub() {
            override fun onFail(p0: Int) {
                onCardDetected.invoke()
            }

            override fun onCardInsert() {
                onCardDetected.invoke()
            }
        })
    }

    override fun init() {
        if (ingenicoService.getDeviceService() == null)
            ingenicoService.bindSdkDeviceService()
    }

    override fun initPrinter() {
        if (ingenicoService.getDeviceService() == null)
            ingenicoService.bindSdkDeviceService()

        val status = printer?.status

        if (status != PrinterError.SUCCESS) run { println("Error $status") }
        else {
            // Set gray
            printer?.setPrnGray(MAX_GRAY_SCALE)

            //Font size normal
            printer?.setHzSize(HZSize.DOT32x24)
            printer?.setHzScale(HZScale.SC3x3)
            printer?.setAscSize(ASCSize.DOT32x12)
            printer?.setAscScale(ASCScale.SC3x3)
        }
    }

    override fun startPrinting() {
        printer?.startPrint(object : OnPrintListener.Stub() {
            @Throws(RemoteException::class)
            override fun onFinish() {
                Log.d("IngenicoAdapter", "onFinish() called")
            }

            @Throws(RemoteException::class)
            override fun onError(i: Int) {
                Log.d("IngenicoAdapter", "onError() called with: i = [${i}]")
            }
        })
    }

    override fun leftIndent(count: Int) {}

    private fun convertToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    override fun printBitmap(bitmap: Bitmap) {
        printer?.addImage(AlignMode.CENTER, convertToByteArray(bitmap))
    }

    override fun printString(string: String) {
        printer?.addText(AlignMode.CENTER, string)
    }

    override fun fontSet() {
    }
}