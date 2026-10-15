package com.example.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.data.QuoteCategory
import com.example.data.RoastQuotesRepository
import java.util.Calendar

object HagagNotificationHelper {

    const val CHANNEL_DAILY_REMINDER = "hagag_daily_reminder_channel"
    const val CHANNEL_ABANDONED_STUDY = "hagag_abandoned_study_channel"

    const val NOTIFICATION_ID_DAILY = 1001
    const val NOTIFICATION_ID_ABANDONED = 1002

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val reminderChannel = NotificationChannel(
                CHANNEL_DAILY_REMINDER,
                "تذكير المذاكرة اليومي لحجاج",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "إشعارات يومية مصرية عشان تفكر حجاج بالمذاكرة وتمنع الكسل"
                enableVibration(true)
            }

            val abandonedChannel = NotificationChannel(
                CHANNEL_ABANDONED_STUDY,
                "زعيق الهروب من المذاكرة",
                NotificationManager.IMPORTANCE_MAX
            ).apply {
                description = "زعيق وتنبيهات فورية لما حجاج يسيب المذاكرة ويهرب للتيك توك"
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(reminderChannel)
            notificationManager.createNotificationChannel(abandonedChannel)
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }

    fun showDailyReminderNotification(context: Context, userName: String = "حجاج") {
        if (!hasNotificationPermission(context)) return

        val quote = RoastQuotesRepository.getRandomQuote(QuoteCategory.START_SESSION)
        val title = "📢 فوق يا $userName.. معاد المذاكرة جه!"
        val message = quote.text.replace("حجاج", userName)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_DAILY_REMINDER)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_DAILY, notification)
        } catch (_: SecurityException) {}
    }

    fun showAbandonedStudyNotification(context: Context, userName: String = "حجاج") {
        if (!hasNotificationPermission(context)) return

        val quotes = listOf(
            "رايح فين يا $userName وسامع صوت التيك توك؟! المذاكرة لسه مخلصتش يا فاشل! ارجع فوراً! 🚨🏃‍♂️",
            "قفلت التطبيق والجلسة شغالة؟! هو الكتاب بيعض يا $userName؟! ارجع اقعد على حيلك! 😡",
            "ألو؟ شرطة تانية إعدادي.. امسكوا $userName سايب البومودورو ورايح يفرك في الصالة! 🚔📚",
            "المؤقت بيعد وإنت هربت؟! السرير مش هيديك درجات في الشهادة! ارجع كمل جلستك! ⏱️"
        )
        val text = quotes.random()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ABANDONED_STUDY)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("🚨 زعيق الهروب من المذاكرة!")
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_ABANDONED, notification)
        } catch (_: SecurityException) {}
    }

    fun scheduleDailyReminder(context: Context, hourOfDay: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, HagagAlarmReceiver::class.java).apply {
            action = "com.example.ACTION_DAILY_REMINDER"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            100,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, HagagAlarmReceiver::class.java).apply {
            action = "com.example.ACTION_DAILY_REMINDER"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            100,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )
        alarmManager.cancel(pendingIntent)
    }
}
