package com.crazylegend.vigilante.di.providers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.crazylegend.collections.isNullOrEmpty
import com.crazylegend.locale.LocaleHelper
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/8/20 to long live and prosper !
 */
@ServiceScoped
class UserNotificationsProvider @Inject constructor(@ServiceContext private val context: Context,
                                                    private val notificationManager: NotificationManagerCompat) {


    /**
     * @param notificationID Int - to cancel once done
     * @return Notification?
     */
    fun buildUsageNotification(notificationID: Int,
                               @StringRes usageTypeString: Int,
                               notificationLEDColorPref: Int,
                               effect: LongArray?,
                               bypassDND: Boolean,
                               isSoundEnabled: Boolean) {

        val usageContentText = LocaleHelper.getLocalizedString(context, usageTypeString)

        val notification = with(NotificationCompat.Builder(context, context.createNotificationChannel(notificationLEDColorPref, effect, bypassDND, isSoundEnabled))) {
            setDefaults(Notification.DEFAULT_LIGHTS)
            setSmallIcon(R.drawable.ic_logo)
            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground))
            setContentTitle(LocaleHelper.getLocalizedString(context, R.string.usage_title))
            setContentText(usageContentText)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(false)
            setOngoing(true)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setColorized(true)
            setVibrate(effect)
            setStyle(NotificationCompat.BigTextStyle().bigText(usageContentText))
            setShowWhen(true)
            setOnlyAlertOnce(true)
            setSilent(!isSoundEnabled)
            if (!isSoundEnabled) {
                setSound(null)
            }
            build()
        }

        notificationManager.notify(notificationID, notification)
    }

    private fun Context.createNotificationChannel(
            notificationLEDColorPref: Int,
            effect: LongArray?,
            bypassDND: Boolean,
            isSoundEnabled: Boolean
    ): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL, getString(R.string.channel_name), NotificationManager.IMPORTANCE_HIGH).apply {
                description = getString(R.string.channel_description)
                enableLights(true)
                setBypassDnd(bypassDND)

                if (!isSoundEnabled) {
                    setSound(null, null)
                }

                vibrationPattern = effect
                enableVibration(!effect.isNullOrEmpty())
                lightColor = notificationLEDColorPref
                //return the same created instance
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
        return NOTIFICATION_CHANNEL
    }

    private companion object {
        private const val NOTIFICATION_CHANNEL = "11"
    }

}