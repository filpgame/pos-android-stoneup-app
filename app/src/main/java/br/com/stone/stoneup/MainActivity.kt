package br.com.stone.stoneup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.stone.pay.core.PAL
import br.com.stone.poladroid.printer.IngenicoAdapter
import br.com.stone.poladroid.printer.PAXAdapter
import br.com.stone.poladroid.printer.VoidAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), MainContract.View {

    private val conditionVariable = ConditionVariable()

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
        PAL.getPrinterProvider()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val animation = AnimationUtils.loadAnimation(this, R.anim.animation_bounce)
        arrowDownImageView.startAnimation(animation)

        PalHelper.tryInitializePal(this)
//        val receiptLayout = findViewById<ConstraintLayout>(R.id.receiptLayout)
        Handler(Looper.getMainLooper()).postDelayed({ presenter.startCardDetection() }, 1500)
    }

    override fun startCamera() {
        startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra("android.intent.extras.CAMERA_FACING", 1)
        }, REQUEST_CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.onCameraResult()
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
    }

    override fun onStop() {
        super.onStop()
        Log.d(this.javaClass.simpleName, "Stopei")
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun generateBitmapFrom(view: View): Bitmap? {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_receipt, null)

        return null
    }
}


