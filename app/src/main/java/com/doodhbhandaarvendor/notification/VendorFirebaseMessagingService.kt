package com.doodhbhandaarvendor.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.doodhbhandaarvendor.R
import com.doodhbhandaarvendor.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class VendorFirebaseMessagingService : FirebaseMessagingService() {


    companion object {
        const val CHANNEL_ID = "fcm_doodhbhandaar"
        val DRIVERS_FOUND = "Driver found"

    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        //Need to update new Token to backend
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notification = message.notification!!
        val params = JSONObject(message.data as Map<*, *>)

        sendNotification(notification, params)
    }
    private fun sendNotification(
        notification: RemoteMessage.Notification,
        params: JSONObject
    ) {
        val notificationBuilder: NotificationCompat.Builder
        var channelId = CHANNEL_ID
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
                    getIntent(params.getString("item_type"), ""), PendingIntent.FLAG_ONE_SHOT
        )
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                channelId = createNotificationChannel()
                notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .setStyle(NotificationCompat.BigTextStyle().bigText(notification.body))
                    .setContentTitle(notification.title)
                    .setContentText(notification.body)
                    .setContentIntent(pendingIntent)
                    .setSound(defaultSoundUri)
                    .setWhen(0)
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setChannelId(channelId)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .setStyle(NotificationCompat.BigTextStyle().bigText(notification.body))
                    .setContentTitle(notification.title)
                    .setContentText(notification.body)
                    .setContentIntent(pendingIntent)
                    .setSound(defaultSoundUri)
                    .setWhen(0)
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            }
            else -> {
                notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .setStyle(NotificationCompat.BigTextStyle().bigText(notification.body))
                    .setContentTitle(notification.title)
                    .setContentText(notification.body)
                    .setContentIntent(pendingIntent)
                    .setSound(defaultSoundUri)
                    .setWhen(0)
            }
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    /*private fun getPenInt(){
        val deepLink = Navigation.findNavController()
    }*/

    private fun getIntent(type: String, id: String): Intent {
        val intent : Intent
        //need to handle via deeplinking rather than intents
        //https://medium.com/incwell-innovations/deeplink-and-navigation-in-android-architecture-component-part-3-b882ed5d5b32
        //https://stackoverflow.com/questions/51148080/deep-link-with-push-notification-fcm-android

        intent = Intent(this, MainActivity::class.java)

//        intent.putExtra(Constants.NOTIFICATION_TYPE_ID, id)
//        intent.putExtra(Constants.NOTIFICATION_TYPE, type)
//        intent.putExtra(Constants.FROM_NOTIFICATION, true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return intent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = CHANNEL_ID
        val channelName = CHANNEL_ID
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        chan.importance = NotificationManager.IMPORTANCE_HIGH
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(chan)
        return channelId
    }

    /*val pendingIntent = if (params.has("item_id")) {
        PendingIntent.getActivity(
            this, 0 *//* Request code *//*,
            getIntent(params.getString("item_type"), params.getString("item_id")),
            PendingIntent.FLAG_ONE_SHOT
        )
    } else {
        PendingIntent.getActivity(
            this, 0 *//* Request code *//*,
            getIntent(params.getString("item_type"), ""), PendingIntent.FLAG_ONE_SHOT
        )
    }*/
}