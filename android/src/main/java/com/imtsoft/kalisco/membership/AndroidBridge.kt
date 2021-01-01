package com.imtsoft.kalisco.membership

import android.os.Handler
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast

class AndroidBridge {

    private val TAG:String = "AndroidBridge"
    private val handler:Handler = Handler();


    @JavascriptInterface
    fun test(stringMessage:String){
        Log.d(TAG,"test")

        handler.post(Runnable {

        })
    }


    /*
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            // Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result
        System.out.println("###############");
        println(token)
        System.out.println("###############");
        view?.loadUrl("javascript:AndroidTest(" + token+")");
        // Log and toast
        // val msg = getString(R.string.msg_token_fmt, token)
        val msg = getString(R.string.msg_token_fmt, token)
        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
    })
    */
}