package com.adr.todo.util

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
import platform.UIKit.UIApplication
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeSystem
import platform.UIKit.UIColor
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIDatePickerStyle
import platform.UIKit.UIView

actual class DateTimeWrapper private constructor(
    private val nsDate: NSDate,
) {
    @OptIn(ExperimentalForeignApi::class)
    actual fun showDatePicker(onDateSelected: (LocalDate?) -> Unit) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            ?: return onDateSelected(null)

        val containerView = UIView().apply {
            backgroundColor = UIColor.whiteColor
            translatesAutoresizingMaskIntoConstraints = false
        }

        val datePicker = UIDatePicker().apply {
            datePickerMode = UIDatePickerMode.UIDatePickerModeDate
            date = nsDate

            // Set preferred style for iOS 13.4+
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

            translatesAutoresizingMaskIntoConstraints = false
        }

        // Create overlay container
        val overlayView = UIView().apply {
            backgroundColor = UIColor.blackColor.colorWithAlphaComponent(0.4)
            translatesAutoresizingMaskIntoConstraints = false
        }

        // Add button container
        val buttonContainer = UIView().apply {
            backgroundColor = UIColor.whiteColor
            translatesAutoresizingMaskIntoConstraints = false
        }

        val cancelButton = UIButton.buttonWithType(UIButtonTypeSystem).apply {
            setTitle("Cancel", UIControlStateNormal)
            // Add action for cancel button
            translatesAutoresizingMaskIntoConstraints = false
        }

        val doneButton = UIButton.buttonWithType(UIButtonTypeSystem).apply {
            setTitle("Done", UIControlStateNormal)
            // Add action for done button
            translatesAutoresizingMaskIntoConstraints = false
        }

        // Add all views to hierarchy and set up constraints

        // Simplified implementation - using direct callbacks
        val components = NSCalendar.currentCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            fromDate = nsDate
        )
        val year = components.year.toInt()
        val month = components.month.toInt()
        val day = components.day.toInt()
        onDateSelected(LocalDate(year, month, day))
    }

    actual fun showTimePicker(onTimeSelected: (LocalTime?) -> Unit) {
        // Simplified implementation - similar to date picker
        val components = NSCalendar.currentCalendar.components(
            NSCalendarUnitHour or NSCalendarUnitMinute,
            fromDate = nsDate
        )
        val hour = components.hour.toInt()
        val minute = components.minute.toInt()
        onTimeSelected(LocalTime(hour, minute))
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

        // Set date components
        components.year = date.year.toLong()
        components.month = date.monthNumber.toLong()
        components.day = date.dayOfMonth.toLong()

        // Set time components
        components.hour = time.hour.toLong()
        components.minute = time.minute.toLong()
        components.second = 0

        // Create NSDate
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