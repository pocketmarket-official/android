package com.imtsoft.kalisco.membership

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushServiceDo(
        private val webView: WebView
) {

    @JavascriptInterface
    fun pushSend(data:String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, token)
        })
    }

    fun pushReceive() {
        webView.evaluateJavascript("""
            console.log('push!!');
        """.trimIndent()) {}
    }

    companion object {
        private const val TAG = "PushServiceDo"
    }
}

class PushService() : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        Log.d(TAG, p0)

        super.onNewToken(p0)
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.notification_kds_channel_name)
            val descriptionText = getString(R.string.notification_kds_channel_desc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("0001", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            var builder = NotificationCompat.Builder(this, "0001")
                    .setSmallIcon(R.drawable.bi)
                    .setContentTitle(it.title)
                    .setContentText(it.body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                var notificationId = System.currentTimeMillis().toInt();
                // notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }


            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    companion object {
        private const val TAG = "PushService"
    }
}