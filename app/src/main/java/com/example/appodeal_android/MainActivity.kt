package com.example.appodeal_android

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.appodeal.ads.Appodeal
import com.appodeal.ads.initializing.ApdInitializationCallback
import com.appodeal.ads.initializing.ApdInitializationError
import com.appodeal.ads.utils.Log as AppodealLog
import com.example.appodeal_android.ui.theme.AppodealAndroidTheme
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.appodeal.ads.NativeAd
import com.appodeal.ads.NativeMediaViewContentType
import com.appodeal.ads.nativead.NativeAdView
import com.appodeal.ads.nativead.NativeAdViewAppWall
import com.appodeal.ads.nativead.NativeAdViewContentStream
import com.appodeal.ads.nativead.NativeAdViewNewsFeed
import com.example.appodeal_android.R

class MainActivity : ComponentActivity() {

    private val appKey = "4de69558a56fde15916e1198dd7e7596569bff99b694d7b9"
    private val TAG = "AppodealTest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set MREC view ID before SDK init
        Appodeal.setMrecViewId(R.id.appodealMrecView)

        setContent {
            AppodealAndroidTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppodealScreen()
                }
            }
        }
    }

    @Composable
    fun AppodealScreen() {
        var isInitialized by remember { mutableStateOf(false) }
        var showMrec by remember { mutableStateOf(false) }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("Appodeal Jetpack Compose Demo")
            }

            item {
                Button(
                    onClick = {
                        if (!isInitialized) {
                            Appodeal.setTesting(true)
                            Appodeal.setLogLevel(AppodealLog.LogLevel.verbose)
                            Appodeal.setPreferredNativeContentType(NativeMediaViewContentType.Auto)

                            val adTypes = Appodeal.BANNER or Appodeal.INTERSTITIAL or
                                    Appodeal.REWARDED_VIDEO or Appodeal.MREC or Appodeal.NATIVE

                            Appodeal.initialize(this@MainActivity, appKey, adTypes,
                                object : ApdInitializationCallback {
                                    override fun onInitializationFinished(errors: List<ApdInitializationError>?) {
                                        if (!errors.isNullOrEmpty()) {
                                            errors.forEach {
                                                Log.e(TAG, "Init error: ${it.message}")
                                            }
                                        } else {
                                            Log.d(TAG, "Appodeal initialized")
                                            isInitialized = true
                                        }
                                    }
                                })
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Initialize Appodeal")
                }
            }

            item {
                Button(
                    onClick = {
                        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
                            Appodeal.show(this@MainActivity, Appodeal.INTERSTITIAL)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Show Interstitial Ad")
                }
            }

            item {
                Button(
                    onClick = {
                        if (Appodeal.isLoaded(Appodeal.BANNER_BOTTOM)) {
                            Appodeal.show(this@MainActivity, Appodeal.BANNER_BOTTOM)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Show Banner Ad")
                }
            }

            item {
                Button(
                    onClick = {
                        if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
                            Appodeal.show(this@MainActivity, Appodeal.REWARDED_VIDEO)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Show Rewarded Ad")
                }
            }



            item {
                var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
                val context = LocalContext.current

                Button(
                    onClick = {
                        if (Appodeal.isLoaded(Appodeal.NATIVE)) {
                            nativeAd = Appodeal.getNativeAds(1).firstOrNull()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Show Native Ad")
                }

                if (nativeAd != null) {
                    AndroidView(
                        factory = { ctx ->
                            NativeAdViewContentStream(ctx).apply {
                                registerView(nativeAd!!)
                            }
                        },
                        update = { it.registerView(nativeAd!!) }
                    )
                }
            }



            item {
                Button(
                    onClick = {
                        if (Appodeal.isLoaded(Appodeal.MREC)) {
                            Appodeal.show(this@MainActivity, Appodeal.MREC)
                        } else {
                            Appodeal.cache(this@MainActivity, Appodeal.MREC)
                        }
                        showMrec = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Show MREC Ad")
                }
            }

            if (showMrec) {
                item {
                    val context = LocalContext.current
                    AndroidView(
                        factory = {
                            Appodeal.getMrecView(context).apply {
                                id = R.id.appodealMrecView
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    250
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                }
            }
        }
    }
}
