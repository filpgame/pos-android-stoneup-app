package br.com.stone.stoneup

import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import br.com.stone.pay.core.PAL
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

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

        val receiptLayout = findViewById<ConstraintLayout>(R.id.receiptLayout)

//        findViewById<ImageView>(R.id.imagePreviewContainer).setImageBitmap(loadBitmapFromView(receiptLayout))
    }

    fun generateBitmapFrom(view: View): Bitmap? {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_receipt, null)

        return null
    }
}


