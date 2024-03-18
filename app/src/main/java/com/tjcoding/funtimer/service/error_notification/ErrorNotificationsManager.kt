package com.tjcoding.funtimer.service.error_notification

import android.content.Context

interface ErrorNotificationsManager {
    fun showErrorNotification(context: Context, errorMessage: String)

    fun removeNotification(context: Context, notificationId: Int)

    companion object {
        const val CHANNEL_ID = "fun_timer_error_channel"

        fun getErrorNotificationId(errorMessage: String): Int {
            return errorMessage.hashCode()
        }
    }
}