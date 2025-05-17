package com.adr.todo.service

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.adr.todo.app.MainActivity
import com.adr.todo.domain.model.Todo
import com.adr.todo.util.Constants
import com.adr.todo.util.DateTimeFormatter
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId

actual class NotificationService(private val context: Context) {
    private val notificationManager = NotificationManagerCompat.from(context)
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = Constants.NOTIFICATION_CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun requestNotificationPermissions(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    Constants.NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    actual fun scheduleNotification(todo: Todo) {
        val reminderDateTime = todo.reminderDateTime ?: return

        if (reminderDateTime < Clock.System.now()) return

        cancelNotification(todo.id)

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(Constants.EXTRA_TODO_ID, todo.id)
            putExtra(Constants.EXTRA_TODO_TITLE, todo.title)
            putExtra(Constants.EXTRA_TODO_DESCRIPTION, todo.description)
            putExtra(Constants.EXTRA_TODO_DUE_DATE, todo.dueDateTime?.toEpochMilliseconds() ?: 0L)
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                todo.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                todo.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val triggerTimeMillis = reminderDateTime.toJavaInstant()
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )
            } catch (e: Exception) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )
            }
        } else {
            try {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )
            } catch (e: Exception) {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )
            }
        }
    }

    actual fun cancelNotification(todoId: Long) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                todoId.toInt(),
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                todoId.toInt(),
                intent,
                PendingIntent.FLAG_NO_CREATE
            )
        }

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }

        notificationManager.cancel(todoId.toInt())
    }

    actual fun showNotification(todo: Todo) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(Constants.EXTRA_TODO_ID, todo.id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context,
                todo.id.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(
                context,
                todo.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        showNotification(todo, pendingIntent)
    }

    fun showNotification(todo: Todo, contentIntent: PendingIntent) {
        val builder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(todo.title)
            .setContentText(todo.description.takeIf { it.isNotEmpty() }
                ?: Constants.REMINDER_FOR_TASK)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        todo.dueDateTime?.let { dueDate ->
            val formattedDueDate = DateTimeFormatter.formatDateTime(dueDate)
            builder.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("${todo.description.takeIf { it.isNotEmpty() } ?: Constants.REMINDER_FOR_TASK}\n\n${Constants.DUE_TEXT}$formattedDueDate")
            )
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(todo.id.toInt(), builder.build())
        } else {
            println(Constants.CANNOT_SHOW_NOTIFICATION)
        }
    }
}