package com.imtsoft.kalisco.membership

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.KeyEvent
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {
    private lateinit var mWebView: WebView
    private val TAG = "MainActiveity"
    private lateinit var token:String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG,"start onCreate")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        mWebView = findViewById(R.id.webView)
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.domStorageEnabled = true

        mWebView.webChromeClient = WebChromeClient()

        mWebView.webViewClient = object: WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

                /*
                var URL:String = request?.url.toString() ;
                if( URL.startsWith("http://localhost") ){

                    URL = URL.replace("http://localhost:3000",BuildConfig.entryUrl);
                    URL = URL.replace("http://localhost:8000",BuildConfig.entryUrl);
                    // URL = BuildConfig.entryUrl + URL.substring(URL.indexOf("http://localhost"),URL.length)
                    Log.d(TAG,"CHANGE URL :: " + URL );
                    view?.loadUrl(URL)
                    return true
                }
                */

                var requestURL = request?.url?.toString()
                if (requestURL?.startsWith(BuildConfig.entryUrl) == true) {
                    view?.loadUrl(requestURL)
                    return true
                }


                // App에서 외부앱을 실행할 때 실행( ex: 카드결제 앱 )
                if (requestURL?.startsWith("intent://") == true) {
                    try {
                        val intent = Intent.parseUri(requestURL, Intent.URI_INTENT_SCHEME)
                        val existPackage = packageManager.getLaunchIntentForPackage(intent.`package`!!)
                        if (existPackage != null) {
                            startActivity(intent)
                        } else {
                            val marketIntent = Intent(Intent.ACTION_VIEW)
                            marketIntent.data = Uri.parse("market://details?id=" + intent.getPackage())
                            startActivity(marketIntent)
                        }
                        return true
                    }catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else if (requestURL?.startsWith("market://") == true) {
                    try {
                        val intent = Intent.parseUri(requestURL, Intent.URI_INTENT_SCHEME)
                        if (intent != null) {
                            startActivity(intent);
                        }
                        return true;
                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }
                }


                return super.shouldOverrideUrlLoading(view, request)
            }

            /**
             * comment : WebView에서 페이지 Load가 끝난후 호출되는 Method
             */
            override fun onPageFinished(view: WebView?, url: String?) {

                if( url.equals(BuildConfig.entryUrl + "/login")){

                    // 웹에서 FcmToken을 저장하는 window함수 호출
                    var jsFunction:String = "javascript:window.makeFcmTokenCookie('$token')"
                    view?.loadUrl(jsFunction)

                } else {
                    super.onPageFinished(view, url)
                }
            }

        }

        mWebView.clearCache(true)

        CookieManager.getInstance().removeSessionCookies() {

            // session, Cookies를 제거후, entryUrl Load
            mWebView.loadUrl(BuildConfig.entryUrl)

            return@removeSessionCookies
        }


        /**
         *  1. Token 가져오기
         */
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            // token = task.result
            token = task.result.toString()
            val msg = token
            Log.d(TAG,"my device Token : " + msg)
        })

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }
}