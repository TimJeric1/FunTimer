package com.tjcoding.funtimer.service.error_notification

import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import com.tjcoding.funtimer.R
import javax.inject.Inject

class ErrorNotificationsManagerImpl @Inject constructor(): ErrorNotificationsManager {
    override fun showErrorNotification(context: Context, errorMessage: String) {
        val nightModeFlags: Int = context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        val isInDarkMode = when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }

        val notification = NotificationCompat.Builder(context, ErrorNotificationsManager.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_pool_24)
            .setColor(if (isInDarkMode) darkColorScheme().primary.toArgb() else lightColorScheme().primary.toArgb())
            .setContentTitle("An Error has occurred")
            .setContentText(errorMessage)
            .setCategory(NotificationCompat.CATEGORY_ERROR)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            .setWhen(0)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(
            ErrorNotificationsManager.getErrorNotificationId(errorMessage),
            notification
        )
    }

    override fun removeNotification(context: Context, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}