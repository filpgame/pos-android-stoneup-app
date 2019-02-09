package br.com.stone.stoneup

import android.content.Context
import android.widget.Toast
import br.com.stone.pay.core.PAL
import br.com.stone.pay.core.PaymentProvider
import br.com.stone.payment.domain.constants.PALResultCode
import br.com.stone.payment.domain.datamodel.TerminalInfo
import br.com.stone.payment.domain.exception.PalException
import br.com.stone.payment.domain.interfaces.DetectCardListener
import br.com.stone.payment.domain.interfaces.ReadCardInfoListener

/**
 * @author felii
 * @since 02/02/2019
 */

object PalHelper {

    private lateinit var paymentProvider: PaymentProvider

    private fun configureTerminalInfo(): TerminalInfo =
        TerminalInfo.builder()
            .isMagStripeFallbackEnabled(false)
            .capabilities("E0F8C8")
            .countryCode("0076")
            .exCapabilities("E000F0A001")
            .isForceOnline("0")
            .getDataPin("1")
            .merchantCategoryCode("0986")
            .referCurrCode("0986")
            .referCurrCon("1000")
            .referCurrExp("2")
            .supportPSESelection("1")
            .terminalType("22")
            .transCurrCode("0986")
            .transCurrExp("2")
            .transType("00")
            .terminalId("28C64D2C38704474")//Mocked value with 16 characters
            .merchantId("28C64D2C3870447490F925880F29A651")
            .merchantName("54455354452053544F4E494E484F")//TESTE STONINHO
            .build()

    fun tryInitializePal(context: Context) {
        val init = PAL.initialize(configureTerminalInfo(), context)
        if (init == PALResultCode.SUCCESS) {
        } else {
            Toast.makeText(context, "ERROR INITIALIZE PAL, CODE ERROR:" + init, Toast.LENGTH_SHORT).show()
        }
    }

    fun startCardDetection(detectCardListener: DetectCardListener) {
        try {
            paymentProvider = PAL.getPaymentProvider()
            paymentProvider.detectCard(detectCardListener)

        } catch (e: PalException) {
            println("${e.message}")
        }
    }

    fun startReadCardInfo(readCardInfoListener: ReadCardInfoListener) {
        paymentProvider.readCardInfo(readCardInfoListener)
    }

    fun stopCardDetection() {
        PAL.getPaymentProvider().cancelReadCard()
    }
}