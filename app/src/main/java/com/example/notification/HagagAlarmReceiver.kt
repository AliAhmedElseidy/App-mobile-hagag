package com.example.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class HagagAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val prefs = context.getSharedPreferences("hagag_study_prefs", Context.MODE_PRIVATE)
        val isReminderEnabled = prefs.getBoolean("daily_reminder_enabled", true)
        val reminderHour = prefs.getInt("reminder_hour", 17) // 5 PM default
        val reminderMinute = prefs.getInt("reminder_minute", 0)
        val userName = prefs.getString("user_name", "حجاج") ?: "حجاج"

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            if (isReminderEnabled) {
                HagagNotificationHelper.createNotificationChannels(context)
                HagagNotificationHelper.scheduleDailyReminder(context, reminderHour, reminderMinute)
            }
            return
        }

        if (isReminderEnabled) {
            HagagNotificationHelper.createNotificationChannels(context)
            HagagNotificationHelper.showDailyReminderNotification(context, userName)
            // Schedule next day
            HagagNotificationHelper.scheduleDailyReminder(context, reminderHour, reminderMinute)
        }
    }
}
