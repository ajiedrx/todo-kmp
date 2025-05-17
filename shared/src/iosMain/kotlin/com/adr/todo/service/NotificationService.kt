package com.adr.todo.service

import com.adr.todo.domain.model.Todo
import com.adr.todo.util.Constants
import kotlinx.datetime.Clock
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSNumber
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.collections.listOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set

actual class NotificationService {
    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

    init {
        notificationCenter.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
            completionHandler = { _, _ -> }
        )
    }

    actual fun scheduleNotification(todo: Todo) {
        val reminderDateTime = todo.reminderDateTime ?: return

        if (reminderDateTime < Clock.System.now()) return

        cancelNotification(todo.id)

        val content = UNMutableNotificationContent().apply {
            setTitle(todo.title)
            setBody(todo.description.takeIf { it.isNotEmpty() } ?: Constants.REMINDER_FOR_TASK)
            setSound(UNNotificationSound.defaultSound())

            val userInfo = mutableMapOf<Any?, Any?>()
            userInfo[KEY_TODO_ID] = NSNumber(todo.id)
            todo.dueDateTime?.let {
                userInfo[KEY_DUE_DATE] = NSNumber(it.toEpochMilliseconds())
            }
            setUserInfo(userInfo)
        }

        val date =
            NSDate.dateWithTimeIntervalSince1970(reminderDateTime.toEpochMilliseconds() / 1000.0)
        val components = NSCalendar.currentCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or
                    NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond,
            fromDate = date
        )

        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            components,
            repeats = false
        )

        val request = UNNotificationRequest.requestWithIdentifier(
            "${Constants.NOTIFICATION_ID_PREFIX}${todo.id}",
            content = content,
            trigger = trigger
        )

        notificationCenter.addNotificationRequest(request, withCompletionHandler = null)
    }

    actual fun cancelNotification(todoId: Long) {
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(
            listOf("${Constants.NOTIFICATION_ID_PREFIX}$todoId")
        )

        notificationCenter.removeDeliveredNotificationsWithIdentifiers(
            listOf("${Constants.NOTIFICATION_ID_PREFIX}$todoId")
        )
    }

    actual fun showNotification(todo: Todo) {
        val content = UNMutableNotificationContent().apply {
            setTitle(todo.title)
            setBody(todo.description.takeIf { it.isNotEmpty() } ?: Constants.REMINDER_FOR_TASK)
            setSound(UNNotificationSound.defaultSound())

            val userInfo = mutableMapOf<Any?, Any?>()
            userInfo[KEY_TODO_ID] = NSNumber(todo.id)
            todo.dueDateTime?.let {
                userInfo[KEY_DUE_DATE] = NSNumber(it.toEpochMilliseconds())
            }
            setUserInfo(userInfo)
        }

        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
            Constants.ONE_SECOND_DELAY,
            repeats = false
        )

        val request = UNNotificationRequest.requestWithIdentifier(
            "${Constants.NOTIFICATION_IMMEDIATE_PREFIX}${todo.id}",
            content = content,
            trigger = trigger
        )

        notificationCenter.addNotificationRequest(request, withCompletionHandler = null)
    }

    companion object {
        private const val KEY_TODO_ID = "todoId"
        private const val KEY_DUE_DATE = "dueDate"
    }
}