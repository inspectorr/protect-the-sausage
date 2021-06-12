package com.inspectorr.sausage

import android.os.Bundle
import android.view.WindowManager

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import android.widget.RelativeLayout
import android.view.Window.FEATURE_NO_TITLE
import com.google.android.gms.ads.*

const val ADMOB_BANNER_ID = "ca-app-pub-7113401163019527/4195029528"
const val ADMOB_TEST_ID = "ca-app-pub-3940256099942544/6300978111"

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuring app
        val config = AndroidApplicationConfiguration().apply {
            useAccelerometer = false
            useCompass = false
            hideStatusBar = true
            useImmersiveMode = true
        }
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        // Create the layout
        val layout = RelativeLayout(this)

        // Do the stuff that initialize() would do for you
        requestWindowFeature(FEATURE_NO_TITLE)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)

        // Create the libgdx view
        val gameView = initializeForView(Game(), config)

        // Create and setup the AdMob view
        val adView = AdView(this).apply {
            adSize = AdSize.BANNER
            adUnitId = if (BuildConfig.DEBUG) ADMOB_TEST_ID else ADMOB_BANNER_ID
            adListener = object: AdListener() {
                override fun onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                override fun onAdFailedToLoad(errorCode : Int) {
                    // Code to be executed when an ad request fails.
                }

                override fun onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                override fun onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                override fun onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            }
            val adRequest = AdRequest.Builder().build()
            loadAd(adRequest)
        }

        // Add the libgdx view
        layout.addView(gameView)

        // Add the AdMob view
        val adParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        layout.addView(adView, adParams)

        // Hook it all up
        setContentView(layout)

        MobileAds.initialize(this) {
            println(it)
            println("id " + adView.adUnitId)
        }
    }
}
