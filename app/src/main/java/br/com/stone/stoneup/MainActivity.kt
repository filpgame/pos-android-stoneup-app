package br.com.stone.stoneup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.stone.stoneup.picture.animationListener
import br.com.stone.stoneup.printer.IngenicoAdapter
import br.com.stone.stoneup.printer.PAXAdapter
import br.com.stone.stoneup.printer.VoidAdapter
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
        cameraImageView.setOnClickListener {
            startCamera()
        }
        pulsator.start()

        startArrowBounceAnimation()
        presenter.init()
        Handler().postDelayed({
            presenter.startCardDetection()
        }, 1000)
        
    }

    private fun startArrowBounceAnimation() {
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        arrowDownImageView.startAnimation(bounceAnimation)
    }

    override fun showIntro() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein)
        fadeIn.animationListener {
            end {
                arrowDownImageView.visibility = View.VISIBLE
                insertCardImageView.visibility = View.VISIBLE
                revolutionImageView.visibility = View.VISIBLE
                pulsator.visibility = View.VISIBLE
                startArrowBounceAnimation()
            }
        }
        arrowDownImageView.startAnimation(fadeIn)
        insertCardImageView.startAnimation(fadeIn)
        revolutionImageView.startAnimation(fadeIn)
        pulsator.startAnimation(fadeIn)
    }

    override fun hideIntro() {
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        fadeOut.animationListener {
            end {
                arrowDownImageView.visibility = View.INVISIBLE
                insertCardImageView.visibility = View.INVISIBLE
                revolutionImageView.visibility = View.INVISIBLE
                pulsator.visibility = View.INVISIBLE
            }
        }
        arrowDownImageView.startAnimation(fadeOut)
        insertCardImageView.startAnimation(fadeOut)
        revolutionImageView.startAnimation(fadeOut)
        pulsator.startAnimation(fadeOut)
    }

    override fun showWelcome() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein)
        fadeIn.animationListener {
            end {
                welcomeImageView.visibility = View.VISIBLE
                messageImageView.visibility = View.VISIBLE
            }
        }
        welcomeImageView.startAnimation(fadeIn)
        messageImageView.startAnimation(fadeIn)
    }

    override fun hideWelcome() {
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        fadeOut.animationListener {
            end {
                welcomeImageView.visibility = View.INVISIBLE
                messageImageView.visibility = View.INVISIBLE
            }
        }
        welcomeImageView.startAnimation(fadeOut)
        messageImageView.startAnimation(fadeOut)
    }

    override fun showPrinting() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein)
        fadeIn.animationListener {
            end {
                printingImageView.visibility = View.VISIBLE
                yourPictureImageView.visibility = View.VISIBLE
            }
        }
        printingImageView.startAnimation(fadeIn)
        yourPictureImageView.startAnimation(fadeIn)
    }

    override fun hidePrinting() {
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        fadeOut.animationListener {
            end {
                printingImageView.visibility = View.INVISIBLE
                yourPictureImageView.visibility = View.INVISIBLE
            }
        }
        printingImageView.startAnimation(fadeOut)
        yourPictureImageView.startAnimation(fadeOut)
    }


    override fun startCamera() {
        startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra("android.intent.extras.CAMERA_FACING", 1)
        }, REQUEST_CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    presenter.onCameraResult(photo)
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


