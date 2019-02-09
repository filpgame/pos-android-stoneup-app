package br.com.stone.stoneup

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.stone.payment.domain.datamodel.CardInfo
import br.com.stone.payment.domain.datamodel.Result
import br.com.stone.payment.domain.exception.PalException
import br.com.stone.payment.domain.interfaces.DetectCardListener
import br.com.stone.payment.domain.interfaces.ReadCardInfoListener
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import br.com.stone.pay.core.PAL
import br.com.stone.poladroid.main.MainContract
import br.com.stone.poladroid.printer.IngenicoAdapter
import br.com.stone.poladroid.printer.PAXAdapter
import br.com.stone.poladroid.printer.VoidAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), MainContract.View, DetectCardListener, ReadCardInfoListener {
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
//        val receiptLayout = findViewById<ConstraintLayout>(R.id.receiptLayout)
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
        val receiptLayout = findViewById<ConstraintLayout>(R.id.receiptLayout)

//        findViewById<ImageView>(R.id.imagePreviewContainer).setImageBitmap(loadBitmapFromView(receiptLayout))

        PalHelper.tryInitializePal(this)
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({ startCardDetection() }, 1500)

    }

    override fun onPause() {
        super.onPause()
        PalHelper.stopCardDetection()
    }

    fun generateBitmapFrom(view: View): Bitmap? {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_receipt, null)

        return null
    }

    private fun startCardDetection() {
        PalHelper.startCardDetection(this)
        PalHelper.startReadCardInfo(this)
    }

    override fun onIccInserted() {
        runOnUiThread {
            Toast.makeText(this, "Icc inserted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCancelledDetection() {
        runOnUiThread {
            Toast.makeText(this, "Icc removed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMagSwiped(p0: Array<out String>?) {
        // Nothing
    }

    override fun onError(p0: PalException?) {
        runOnUiThread {
            Toast.makeText(this, "${p0?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCardReading() {

    }

    override fun onCardRemoved() {
        startCardDetection()
    }

    override fun onError(p0: Result?) {

    }

    override fun onRemoveCard() {

    }

    override fun onCardInfo(p0: CardInfo?) {

    }
}


