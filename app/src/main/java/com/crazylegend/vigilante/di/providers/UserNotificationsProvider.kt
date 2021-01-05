package com.crazylegend.vigilante.di.providers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import com.crazylegend.kotlinextensions.context.notificationManager
import com.crazylegend.kotlinextensions.locale.LocaleHelper
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/8/20 to long live and prosper !
 */
@ServiceScoped
class UserNotificationsProvider @Inject constructor(@ServiceContext private val context: Context,
                                                    private val prefsProvider: PrefsProvider) {

    private val bypassDND get() = prefsProvider.isBypassDNDEnabled

    /**
     * @param notificationID Int - to cancel once done
     * @return Notification?
     */
    fun buildUsageNotification(notificationID: Int, @StringRes usageTypeString: Int, notificationLEDColorPref: Int, effect: LongArray?) {

        val usageContentText = LocaleHelper.getLocalizedString(context, usageTypeString)

        val notificationCompatBuilder = NotificationCompat.Builder(context, context.createNotificationChannel(notificationLEDColorPref))
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground))
                .setContentTitle(LocaleHelper.getLocalizedString(context, R.string.usage_title))
                .setContentText(usageContentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColorized(true)
                .setVibrate(effect)
                .setStyle(NotificationCompat.BigTextStyle().bigText(usageContentText))
                .setShowWhen(true)
                .setOnlyAlertOnce(true)
                .setSound(null)

        context.notificationManager?.notify(notificationID, notificationCompatBuilder.build())
    }

    private fun Context.createNotificationChannel(notificationLEDColorPref: Int): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = with(NotificationChannel(NOTIFICATION_CHANNEL, getString(R.string.channel_name), NotificationManager.IMPORTANCE_HIGH)) {
                description = getString(R.string.channel_description)
                description = getString(R.string.channel_description)
                enableLights(true)
                setBypassDnd(bypassDND)
                lightColor = notificationLEDColorPref
                //return the same created instance
                this
            }
            notificationManager?.createNotificationChannel(notificationChannel)
        }
        return NOTIFICATION_CHANNEL
    }

    companion object {
        private const val NOTIFICATION_CHANNEL = "11"
    }

}