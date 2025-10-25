package com.adr.todo.util

import com.adr.todo.ContextFactory
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import kotlinx.cinterop.memScoped
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSOperatingSystemVersion
import platform.Foundation.NSProcessInfo
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleActionSheet
import platform.UIKit.UIApplication
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIDatePickerStyle

actual class DateTimeWrapper private constructor(
    private val nsDate: NSDate,
) {
    @OptIn(ExperimentalForeignApi::class)
    actual fun showDatePicker(context: ContextFactory, onDateSelected: (LocalDate?) -> Unit) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            ?: return onDateSelected(null)

        val alertController = UIAlertController.alertControllerWithTitle(
            title = "Select Date",
            message = "\n\n\n\n\n\n\n\n",
            preferredStyle = UIAlertControllerStyleActionSheet
        )

        val datePicker = UIDatePicker().apply {
            datePickerMode = UIDatePickerMode.UIDatePickerModeDate
            date = nsDate

            if (NSProcessInfo.processInfo.isOperatingSystemAtLeastVersion(
                    memScoped {
                        cValue<NSOperatingSystemVersion> {
                            majorVersion = 13L
                            minorVersion = 4L
                            patchVersion = 0L
                        }
                    }
                )
            ) {
                preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
            }
        }

        alertController.view.addSubview(datePicker)

        val doneAction = UIAlertAction.actionWithTitle(
            title = "Done",
            style = UIAlertActionStyleDefault
        ) { _ ->
            val selectedDate = datePicker.date
            val components = NSCalendar.currentCalendar.components(
                NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
                fromDate = selectedDate
            )
            val year = components.year.toInt()
            val month = components.month.toInt()
            val day = components.day.toInt()
            onDateSelected(LocalDate(year, month, day))
        }
        alertController.addAction(doneAction)

        val cancelAction = UIAlertAction.actionWithTitle(
            title = "Cancel",
            style = UIAlertActionStyleCancel
        ) { _ ->
            onDateSelected(null)
        }
        alertController.addAction(cancelAction)

        rootViewController.presentViewController(alertController, animated = true, completion = null)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun showTimePicker(context: ContextFactory, onTimeSelected: (LocalTime?) -> Unit) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            ?: return onTimeSelected(null)

        val alertController = UIAlertController.alertControllerWithTitle(
            title = "Select Time",
            message = "\n\n\n\n\n\n\n\n",
            preferredStyle = UIAlertControllerStyleActionSheet
        )

        val timePicker = UIDatePicker().apply {
            datePickerMode = UIDatePickerMode.UIDatePickerModeTime
            date = nsDate

            if (NSProcessInfo.processInfo.isOperatingSystemAtLeastVersion(
                    memScoped {
                        cValue<NSOperatingSystemVersion> {
                            majorVersion = 13L
                            minorVersion = 4L
                            patchVersion = 0L
                        }
                    }
                )
            ) {
                preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
            }
        }

        alertController.view.addSubview(timePicker)

        val doneAction = UIAlertAction.actionWithTitle(
            title = "Done",
            style = UIAlertActionStyleDefault
        ) { _ ->
            val selectedDate = timePicker.date
            val components = NSCalendar.currentCalendar.components(
                NSCalendarUnitHour or NSCalendarUnitMinute,
                fromDate = selectedDate
            )
            val hour = components.hour.toInt()
            val minute = components.minute.toInt()
            onTimeSelected(LocalTime(hour, minute))
        }
        alertController.addAction(doneAction)

        val cancelAction = UIAlertAction.actionWithTitle(
            title = "Cancel",
            style = UIAlertActionStyleCancel
        ) { _ ->
            onTimeSelected(null)
        }
        alertController.addAction(cancelAction)

        rootViewController.presentViewController(alertController, animated = true, completion = null)
    }

    actual fun getDate(): LocalDate {
        val components = NSCalendar.currentCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            fromDate = nsDate
        )
        return LocalDate(
            components.year.toInt(),
            components.month.toInt(),
            components.day.toInt()
        )
    }

    actual fun getTime(): LocalTime {
        val components = NSCalendar.currentCalendar.components(
            NSCalendarUnitHour or NSCalendarUnitMinute,
            fromDate = nsDate
        )
        return LocalTime(
            components.hour.toInt(),
            components.minute.toInt()
        )
    }

    actual fun combineDateTime(date: LocalDate, time: LocalTime): Instant {
        val calendar = NSCalendar.currentCalendar
        val components = NSDateComponents()

        components.year = date.year.toLong()
        components.month = date.monthNumber.toLong()
        components.day = date.dayOfMonth.toLong()

        components.hour = time.hour.toLong()
        components.minute = time.minute.toLong()
        components.second = 0

        val resultDate = calendar.dateFromComponents(components) ?: NSDate()

        return Instant.fromEpochMilliseconds((resultDate.timeIntervalSince1970 * 1000).toLong())
    }

    actual companion object {
        actual fun fromInstant(instant: Instant): DateTimeWrapper {
            val nsDate =
                NSDate.dateWithTimeIntervalSince1970(instant.toEpochMilliseconds() / 1000.0)
            return DateTimeWrapper(nsDate)
        }

        fun createPlatformInstance(): DateTimeWrapper {
            return DateTimeWrapper(NSDate())
        }
    }
}