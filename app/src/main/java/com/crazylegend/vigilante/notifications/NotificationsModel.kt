package com.crazylegend.vigilante.notifications

/**
 * Created by crazy on 10/20/20 to long live and prosper !
 */
data class NotificationsModel(
        val title: String?, val bigText: String?, val text: String?, val visibility: Int?,
        val category: String?, val color: Int?, val flags: Int?, val group: String?, val channelId: String?,
        val sentByPackage: String?,
        val showTime: Long
) {
}