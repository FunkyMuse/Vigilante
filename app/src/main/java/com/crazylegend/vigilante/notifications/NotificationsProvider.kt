package com.crazylegend.vigilante.notifications

import android.app.Notification
import android.content.Context
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import com.crazylegend.kotlinextensions.currentTimeMillis
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 10/20/20 to long live and prosper !
 */
@ServiceScoped
class NotificationsProvider @Inject constructor(
        @ServiceContext private val context: Context) {


    fun processEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            val notification = event.parcelableData as? Notification
            val extras = notification?.extras
            val title = extras?.getString("android.title", null)
            val bigText = extras?.getString("android.bigText", null)
            val text = extras?.getString("android.text", null)
            val visibility = notification?.visibility
            val category = notification?.category
            val color = notification?.color
            val flags = notification?.flags
            val group = notification?.group
            val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification?.channelId
            } else {
                null
            }
            val sentByPackage = event.packageName?.toString()
            val notificationModel = createNotificationModel(title, bigText, text, visibility, category, color, flags, group, channelId,
                    sentByPackage, currentTimeMillis)
            saveNotification(notificationModel)
        }
    }

    private fun saveNotification(notificationModel: NotificationsModel) {

    }

    private fun createNotificationModel(title: String?, bigText: String?, text: String?, visibility: Int?,
                                        category: String?, color: Int?, flags: Int?, group: String?, channelId: String?,
                                        sentByPackage: String?,
                                        currentTimeMillis: Long) = NotificationsModel(title, bigText, text,
            visibility, category, color, flags, group, channelId, sentByPackage, currentTimeMillis)


}