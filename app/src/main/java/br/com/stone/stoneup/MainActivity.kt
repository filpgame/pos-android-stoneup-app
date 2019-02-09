package br.com.stone.stoneup

import android.content.Context
import android.graphics.Bitmap
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

class MainActivity : AppCompatActivity(), DetectCardListener, ReadCardInfoListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val animation = AnimationUtils.loadAnimation(this, R.anim.animation_bounce)
        arrowDownImageView.startAnimation(animation)
//        printerProvider.print()

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


