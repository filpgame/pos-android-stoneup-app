package br.com.stone.poladroid.main

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
    }

    interface Presenter {
        fun printPicture(bitmap: Bitmap)
    }
}