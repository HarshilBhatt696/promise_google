package my.promise.harshil.Service


import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import my.promise.harshil.FireabaseClass
import my.promise.harshil.References
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import my.promise.harshil.Util.FireStoreUtil


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            //TODO: Show notification if we're not online
            remoteMessage.messageId
//            FireabaseClass.myRef.child("Message Recived").setValue(remoteMessage.notification?.title.toString())
//            FireabaseClass.myRef.child("Message Recived1").setValue(remoteMessage.notification?.body.toString())
          //  FireabaseClass.myRef.child("Message Recived2").setValue(remoteMessage.notification?..toString())


//            if (remoteMessage.notification?.title.toString() == "Something") {
//                FireStoreUtil.RemoveTodaysUsers()
//
//            }

//            FireabaseClass.myRef.child(References.ExtraDelete3.toString()).setValue("Notification")
//            Notis(this, "Hey    " , "WORLD" , 4)
           // Log.d("FCM", remoteMessage.data?.toString())

        }
    }
}

fun Notis(Cont: Context, message: String, Titlte : String, number: Int) {
    createNotificationChannel(Cont)
    val builder = NotificationCompat.Builder(Cont ,  "new request")
        .setSmallIcon(R.drawable.alert_dark_frame)
        .setContentTitle(Titlte)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText(message))



    with(NotificationManagerCompat.from(Cont)) {
        // notificationId is a unique int for each notification that you must define
        notify(117, builder.build())
    }

}

private fun createNotificationChannel(Cont : Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = ("new request")
        val descriptionText = (R.string.cancel)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("new request", name, importance).apply {
            description = "This is wonders"
        }
        // Register the channel with the system
        val notificationManager: NotificationManager = Cont.getSystemService(Context.NOTIFICATION_SERVICE ) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}