package com.example.appodeal_android

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.appodeal.ads.Appodeal
import com.appodeal.ads.MrecView
import com.appodeal.ads.NativeAd
import com.appodeal.ads.initializing.ApdInitializationCallback
import com.appodeal.ads.initializing.ApdInitializationError
import com.example.appodeal_android.ui.theme.AppodealAndroidTheme

class MainActivity : ComponentActivity() {
    private val TAG = "AppodealTest"
    private val APP_KEY = "4de69558a56fde15916e1198dd7e7596569bff99b694d7b9"
    private var nativeAds: List<NativeAd>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppodealAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppodealScreen(
                        modifier = Modifier.padding(innerPadding),
                        onInitClicked = { initializeAppodeal() },
                        onShowBannerClicked = { showBannerAd() },
                        onShowInterstitialClicked = { showInterstitialAd() },
                        onShowRewardedClicked = { showRewardedVideoAd() },
                        onShowMrecClicked = { showMrecAd() },
                        onLoadNativeClicked = { loadNativeAds() }
                    )
                }
            }
        }
    }

    private fun initializeAppodeal() {
        Log.d(TAG, "Starting Appodeal initialization")
        Appodeal.setTesting(true) // Enable test mode
        val adTypes = Appodeal.BANNER or Appodeal.INTERSTITIAL or Appodeal.REWARDED_VIDEO or
                Appodeal.MREC or Appodeal.NATIVE
        Appodeal.initialize(this, APP_KEY, adTypes, object : ApdInitializationCallback {
            override fun onInitializationFinished(errors: List<ApdInitializationError>?) {
                if (errors != null && errors.isNotEmpty()) {
                    errors.forEach { error -> Log.e(TAG, "Init Error: ${error.message}") }
                } else {
                    Log.d(TAG, "Appodeal initialized successfully")
                }
            }
        })
    }

    private fun showBannerAd() {
        Log.d(TAG, "Attempting to show Banner")
        if (Appodeal.isLoaded(Appodeal.BANNER)) {
            Appodeal.show(this, Appodeal.BANNER_BOTTOM)
            Log.d(TAG, "Banner displayed")
        } else {
            Log.d(TAG, "Banner not loaded yet")
        }
    }

    private fun showInterstitialAd() {
        Log.d(TAG, "Attempting to show Interstitial")
        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
            Appodeal.show(this, Appodeal.INTERSTITIAL)
            Log.d(TAG, "Interstitial displayed")
        } else {
            Log.d(TAG, "Interstitial not loaded yet")
        }
    }

    private fun showRewardedVideoAd() {
        Log.d(TAG, "Attempting to show Rewarded Video")
        if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
            Appodeal.show(this, Appodeal.REWARDED_VIDEO)
            Log.d(TAG, "Rewarded Video displayed")
        } else {
            Log.d(TAG, "Rewarded Video not loaded yet")
        }
    }

    private fun showMrecAd() {
        Log.d(TAG, "Attempting to show MREC")
        if (Appodeal.isLoaded(Appodeal.MREC)) {
            Log.d(TAG, "MREC loaded and will be displayed in UI")
        } else {
            Log.d(TAG, "MREC not loaded yet")
        }
    }

    private fun loadNativeAds() {
        Log.d(TAG, "Attempting to load Native Ads")
        Appodeal.getNativeAds(1)
    }
}

@Composable
fun AppodealScreen(
    modifier: Modifier = Modifier,
    onInitClicked: () -> Unit,
    onShowBannerClicked: () -> Unit,
    onShowInterstitialClicked: () -> Unit,
    onShowRewardedClicked: () -> Unit,
    onShowMrecClicked: () -> Unit,
    onLoadNativeClicked: () -> Unit
) {
    var showMrec by remember { mutableStateOf(false) } // Toggle MREC visibility

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Appodeal Test App",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        item {
            Button(onClick = onInitClicked, modifier = Modifier.fillMaxWidth()) {
                Text("Initialize Appodeal")
            }
        }
        item {
            Button(onClick = onShowBannerClicked, modifier = Modifier.fillMaxWidth()) {
                Text("Show Banner Ad")
            }
        }
        item {
            Button(onClick = onShowInterstitialClicked, modifier = Modifier.fillMaxWidth()) {
                Text("Show Interstitial Ad")
            }
        }
        item {
            Button(onClick = onShowRewardedClicked, modifier = Modifier.fillMaxWidth()) {
                Text("Show Rewarded Video Ad")
            }
        }
        item {
            Button(
                onClick = {
                    onShowMrecClicked()
                    showMrec = true // Show MREC when button is clicked
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show MREC Ad")
            }
        }
        item {
            Button(onClick = onLoadNativeClicked, modifier = Modifier.fillMaxWidth()) {
                Text("Load Native Ad")
            }
        }
        // MREC Ad View
        if (showMrec) {
            item {
                AndroidView(
                    factory = { context ->
                        MrecView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            Appodeal.setMrecViewId(1)
                            //Appodeal.setMrecViewId(R.id.appodealMrecView)
                            Appodeal.show(context as ComponentActivity, Appodeal.MREC)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp) // MREC standard size is 300x250
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
public fun AppodealScreenPreview() {
    AppodealAndroidTheme {
        AppodealScreen(
            onInitClicked = {},
            onShowBannerClicked = {},
            onShowInterstitialClicked = {},
            onShowRewardedClicked = {},
            onShowMrecClicked = {},
            onLoadNativeClicked = {}
        )
    }
}