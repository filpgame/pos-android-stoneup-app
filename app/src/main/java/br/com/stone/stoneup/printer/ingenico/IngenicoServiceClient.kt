package br.com.stone.poladroid.printer.ingenico

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import com.usdk.apiservice.aidl.UDeviceService
import com.usdk.apiservice.aidl.printer.UPrinter

class IngenicoServiceClient(val context: Context) {

    companion object {
        private val SDK_ACTION = "com.usdk.apiservice"
        private val EMV_LOG = "emvLog"
        private val COMMON_LOG = "commonLog"
        private val IBINDER_FLAG = 0
        val MAX_GRAY_SCALE = 10
    }

    private var mPrinter: UPrinter? = null
    private var mDeviceService: UDeviceService? = null

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            mDeviceService = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            try {
                mDeviceService = UDeviceService.Stub.asInterface(service)
                mDeviceService?.register(null, Binder())
                val bundle = Bundle()
                bundle.putBoolean(EMV_LOG, true)
                bundle.putBoolean(COMMON_LOG, true)
                mDeviceService?.debugLog(bundle)
                mPrinter = UPrinter.Stub.asInterface(mDeviceService?.printer)
                linkToDeath(service)

            }catch (e: Exception) {
                e.printStackTrace()
            }

        }

        @Throws(RemoteException::class)
        private fun linkToDeath(service: IBinder) {
            service.linkToDeath({
                bindSdkDeviceService()
            }, IBINDER_FLAG)
        }
    }

    fun bindSdkDeviceService(){
        val intent = Intent()
        intent.action = SDK_ACTION
        intent.`package` = SDK_ACTION
        val flag = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        if (!flag) {
            throw RuntimeException(" - Error instantiating Ingenico Service")
        }
    }

    fun getPrinter(): UPrinter? {
        //FIXME below its very ugly
        while (mPrinter == null){
            println("waiting bind")
        }
      return mPrinter
    }

    fun getDeviceService(): UDeviceService? = mDeviceService
}