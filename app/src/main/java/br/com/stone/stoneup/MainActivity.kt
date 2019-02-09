package br.com.stone.stoneup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import br.com.stone.pay.core.PAL
import br.com.stone.poladroid.main.MainContract
import br.com.stone.poladroid.printer.IngenicoAdapter
import br.com.stone.poladroid.printer.PAXAdapter
import br.com.stone.poladroid.printer.VoidAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), MainContract.View {
    override val imageWidth: Int get() = 375
    override val imageHeight: Int get() = 500
    override val viewContext: Context get() = applicationContext

    private val REQUEST_CAMERA = 1034
    private val presenter: MainContract.Presenter by lazy {
        val adapter = when (Build.MODEL) {
            "A920" -> PAXAdapter(this)
            "APOS A8OVS",
            "APOS A8" -> IngenicoAdapter(this)
            else -> VoidAdapter()
        }
        MainPresenter(this, adapter)
    }
    private var pictureWasTaken = false

    private val printerProvider by lazy {
        PalHelper.tryInitializePal(this)
        PAL.getPrinterProvider()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val animation = AnimationUtils.loadAnimation(this, R.anim.animation_bounce)
        arrowDownImageView.startAnimation(animation)
        insertCardTextView.setOnClickListener {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra("android.intent.extras.CAMERA_FACING", 1)
            }, REQUEST_CAMERA)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    presenter.printPicture(photo)
                }
                else -> toast("Unknown request")
            }
        }
    }
}


