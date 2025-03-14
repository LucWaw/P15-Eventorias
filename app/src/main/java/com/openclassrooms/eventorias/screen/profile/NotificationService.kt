package com.openclassrooms.eventorias.screen.profile

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.openclassrooms.eventorias.MainActivity
import com.openclassrooms.eventorias.R

class NotificationService(private val context: Context) {

    val channelAllID = "Firebase All Messaging ID"
    val channelName = "Firebase Messaging"


    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendNotification(title: String?, body: String?) {
        val channelId = channelAllID
        val channelName = channelName

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        Log.d("NotificationService", "Sending notification: $title - $body")

        pendingIntent.send()

        notificationManager.notify(1, notification)
    }

    fun enableNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Firebase Messaging ID",
                "Firebase Messaging",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun disableNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel("Firebase Messaging ID")
        }
    }




}