package br.com.stone.poladroid.printer

import android.content.Context
import android.graphics.Bitmap
import android.os.RemoteException
import br.com.stone.poladroid.printer.ingenico.IngenicoServiceClient
import br.com.stone.poladroid.printer.ingenico.IngenicoServiceClient.Companion.MAX_GRAY_SCALE
import com.usdk.apiservice.aidl.printer.ASCScale
import com.usdk.apiservice.aidl.printer.ASCSize
import com.usdk.apiservice.aidl.printer.AlignMode
import com.usdk.apiservice.aidl.printer.HZScale
import com.usdk.apiservice.aidl.printer.HZSize
import com.usdk.apiservice.aidl.printer.OnPrintListener
import com.usdk.apiservice.aidl.printer.PrinterError
import java.io.ByteArrayOutputStream

/**
 * @author filpgame
 * @since 2017-08-07
 */
class IngenicoAdapter(var context: Context) : PrinterAdapter {
    val printer: IngenicoServiceClient by lazy { IngenicoServiceClient(context) }
    override fun step(count: Int) {
        printer.getPrinter()?.feedPix(count)
    }

    override fun initPrinter() {

        if(printer.getDeviceService() == null)
            printer.bindSdkDeviceService()

        val status = printer.getPrinter()?.status

        if (status != PrinterError.SUCCESS) run { println("Error $status") }
        else {
            // Set gray
            printer.getPrinter()?.setPrnGray(MAX_GRAY_SCALE)

            //Font size normal
            printer.getPrinter()?.setHzSize(HZSize.DOT32x24)
            printer.getPrinter()?.setHzScale(HZScale.SC3x3)
            printer.getPrinter()?.setAscSize(ASCSize.DOT32x12)
            printer.getPrinter()?.setAscScale(ASCScale.SC3x3)
        }
    }

    override fun startPrinting() {
        printer.getPrinter()?.startPrint(object : OnPrintListener.Stub() {
            @Throws(RemoteException::class)
            override fun onFinish() {
                println("Success printing")
            }

            @Throws(RemoteException::class)
            override fun onError(i: Int) {
                println("Error Printing $i")
            }
        })
    }

    override fun leftIndent(count: Int) {

    }

    fun convertToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    override fun printBitmap(bitmap: Bitmap) {
        printer.getPrinter()?.addImage(AlignMode.CENTER, convertToByteArray(bitmap))
    }

    override fun printString(string: String) {
        printer.getPrinter()?.addText(AlignMode.CENTER, string)
    }

    override fun fontSet() {
    }
}