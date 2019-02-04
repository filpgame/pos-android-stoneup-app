package br.com.stone.stoneup

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import br.com.stone.pay.core.PAL
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val printerProvider by lazy {
        PalHelper.tryInitializePal(this)
        PAL.getPrinterProvider()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val animation = AnimationUtils.loadAnimation(this, R.anim.animation_bounce)
        arrowDownImageView.startAnimation(animation)
//        printerProvider.print()
    }
}


