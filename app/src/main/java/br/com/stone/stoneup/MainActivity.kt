package br.com.stone.stoneup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.stone.poladroid.animationListener
import br.com.stone.poladroid.printer.IngenicoAdapter
import br.com.stone.poladroid.printer.PAXAdapter
import br.com.stone.poladroid.printer.VoidAdapter
import br.com.stone.poladroid.show
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val animation = AnimationUtils.loadAnimation(this, R.anim.animation_bounce)
//        arrowDownImageView.startAnimation(animation)
        takePictureButton.setOnClickListener {
            startCamera()
        }
    }

    private fun showWelcome() {
        val welcomeAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_slidedown_fadein)
        welcomeAnimation.animationListener { start { welcomeTextView.show() } }
        welcomeAnimation.fillAfter = true

        val stoneUpAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_slideup)
        stoneUpAnimation.fillAfter = true

        welcomeTextView.startAnimation(welcomeAnimation)
        stoneUpImageView.startAnimation(stoneUpAnimation)
        takePictureButton.visibility = View.INVISIBLE
        Handler().postDelayed({
            hideWelcome()
        }, 5000)
    }

    private fun hideWelcome() {
        val welcomeAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_slidedown_fadein)
        welcomeAnimation.animationListener { start { welcomeTextView.show() } }
        welcomeAnimation.fillAfter = true
        welcomeAnimation.interpolator = Interpolator {
            Math.abs(it - 1f)
        }

        val stoneUpAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_slideup)
        stoneUpAnimation.fillAfter = true
        stoneUpAnimation.interpolator = Interpolator {
            Math.abs(it - 1f)
        }
        welcomeTextView.startAnimation(welcomeAnimation)
        stoneUpImageView.startAnimation(stoneUpAnimation)
        takePictureButton.show()
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
                    showWelcome()
                }
                else -> toast("Unknown request")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(this.javaClass.simpleName, "Stopei")
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


