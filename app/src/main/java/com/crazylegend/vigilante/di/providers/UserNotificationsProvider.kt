package com.crazylegend.vigilante.di.providers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import com.crazylegend.kotlinextensions.context.getAppName
import com.crazylegend.kotlinextensions.context.notificationManager
import com.crazylegend.kotlinextensions.locale.LocaleHelper
import com.crazylegend.kotlinextensions.tryOrNull
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/8/20 to long live and prosper !
 */
@ServiceScoped
class UserNotificationsProvider @Inject constructor(@ServiceContext private val context: Context) {

    /**
     * @param usageTypeTitle Int - ex Camera/mic is currently being used
     * @param appPackage String - app package
     * @param usingType Int - camera/mic
     * @param notificationID Int - to cancel once done
     * @return Notification?
     */
    fun buildUsageNotification(@StringRes usageTypeTitle: Int, appPackage: String, @StringRes usingType: Int,
                               notificationID: Int) {

        val usageTypeString = LocaleHelper.getLocalizedString(context, usingType) ?: ""
        val packageName = tryOrNull { context.getAppName(appPackage) } ?: ""
        val contentText = LocaleHelper.getLocalizedString(context, R.string.currently_using_type_content)
                ?: ""
        val usageContentText = "$packageName $contentText $usageTypeString"

        val notificationCompatBuilder = NotificationCompat.Builder(
                context,
                context.createNotificationChannel()
        )
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground))
                .setContentTitle(LocaleHelper.getLocalizedString(context, usageTypeTitle))
                .setContentText(usageContentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColorized(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(usageContentText))
                .setShowWhen(true)
                .setOnlyAlertOnce(true)
                .setSound(null)

        context.notificationManager?.notify(notificationID, notificationCompatBuilder.build())
    }

    private val NOTIFICATION_CHANNEL = "11"
    private fun Context.createNotificationChannel(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL, getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = getString(R.string.channel_description)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.MAGENTA
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        return NOTIFICATION_CHANNEL
    }

}