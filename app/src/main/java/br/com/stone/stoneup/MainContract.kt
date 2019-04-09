package br.com.stone.stoneup

import android.content.Context
import android.graphics.Bitmap

/**
 * @author filpgame
 * @since 2017-07-10
 */
interface MainContract {
    interface View {
        val viewContext: Context
        val imageWidth: Int
        val imageHeight: Int

        fun startCamera()
        fun showErrorMessage(message: String)
    }

    interface Presenter {
        fun startCardDetection()
        fun printPicture(bitmap: Bitmap)
        fun onCameraResult()
    }
}