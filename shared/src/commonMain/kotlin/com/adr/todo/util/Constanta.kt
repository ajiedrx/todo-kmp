package com.adr.todo.util

object Constants {
    // General
    const val APP_NAME = "ToDo App"
    const val EMPTY_STRING = ""
    const val HYPHEN = "-"
    const val COLON = ":"
    const val SPACE = " "
    const val COMMA = ","

    // Time
    const val HOUR_IN_MILLIS = 60 * 60 * 1000L
    const val ONE_SECOND_DELAY = 1.0

    // UI
    const val SMALL_PADDING = 4
    const val MEDIUM_PADDING = 8
    const val LARGE_PADDING = 16
    const val XLARGE_PADDING = 24
    const val ICON_SIZE = 24
    const val LOADING_INDICATOR_SIZE = 50
    const val FAB_SPACING = 80
    const val TEXT_FIELD_HEIGHT = 120
    const val MIN_HEIGHT_BOX = 100
    const val CARD_ELEVATION = 2
    const val CARD_VERTICAL_PADDING = 4

    // Alpha values
    const val SURFACE_ALPHA = 0.7f
    const val SURFACE_VARIANT_LOW_ALPHA = 0.1f
    const val SURFACE_VARIANT_MEDIUM_ALPHA = 0.3f
    const val SURFACE_VARIANT_HIGH_ALPHA = 0.5f
    const val TEXT_MEDIUM_ALPHA = 0.6f
    const val TEXT_HIGH_ALPHA = 0.7f
    const val ERROR_ALPHA = 0.8f

    // Text messages
    const val GREETING_MORNING = "Good Morning"
    const val GREETING_AFTERNOON = "Good Afternoon"
    const val GREETING_EVENING = "Good Evening"
    const val TASKS_TITLE = "My Tasks"
    const val TODAY_SECTION = "TODAY"
    const val LATER_SECTION = "LATER"
    const val NO_TASKS_TODAY = "No tasks for today"
    const val NO_UPCOMING_TASKS = "No upcoming tasks"
    const val DELETE_TASK = "Delete"
    const val TASK_DELETED = "Task deleted"
    const val TASK_RESTORED = "Task restored"
    const val CREATE_TASK = "Create Task"
    const val EDIT_TASK = "Edit Task"
    const val TASK_DETAILS = "Task Details"
    const val MARK_COMPLETED = "Mark as completed"
    const val COMPLETED = "Completed"
    const val DELETE_CONFIRMATION_TITLE = "Delete Task"
    const val DELETE_CONFIRMATION_MESSAGE =
        "Are you sure you want to delete this task? This action cannot be undone."
    const val DISCARD_CONFIRMATION_TITLE = "Discard Changes"
    const val DISCARD_CONFIRMATION_MESSAGE =
        "You have unsaved changes. Are you sure you want to discard them?"
    const val TITLE_LABEL = "Title"
    const val TITLE_REQUIRED = "Title is required"
    const val DESCRIPTION_LABEL = "Description"
    const val DUE_DATE_LABEL = "Due Date and Time"
    const val DUE_DATE_REQUIRED = "Due date is required"
    const val REMINDER_LABEL = "Reminder"
    const val REMINDER_BEFORE_DUE = "Reminder must be before due time"
    const val REMINDER_IN_FUTURE = "Reminder must be in the future"
    const val SAVE = "Save"
    const val CANCEL = "Cancel"
    const val KEEP_EDITING = "Keep Editing"
    const val DISCARD = "Discard"
    const val CREATED = "Created"
    const val LAST_UPDATED = "Last Updated"
    const val CUSTOM_REMINDER = "Custom Reminder Time"
    const val SELECT_DATE_TIME = "Select Date and Time"
    const val CLEAR = "Clear"
    const val BACK = "Back"
    const val EDIT = "Edit"
    const val UNDO = "Undo"
    const val REMINDER_TEXT = "Reminder: "
    const val ERROR_TEXT = "Error"
    const val DISMISS_TEXT = "Dismiss"
    const val DUE_TEXT = "Due: "
    const val SET_DUE_DATE_FIRST = "Set due date first"
    const val NO_REMINDER = "No reminder"
    const val SELECT_REMINDER = "Select reminder"
    const val CUSTOM_PREFIX = "Custom: "
    const val CREATE_MODE_TITLE = "Create Task"
    const val EDIT_MODE_TITLE = "Edit Task"
    const val VIEW_MODE_TITLE = "Task Details"
    const val REQUIRED_SUFFIX = " *"
    const val COMPLETED_TASKS = "Completed Tasks"
    const val NO_COMPLETED_TASKS = "No completed tasks yet"
    const val RESTORE = "Restore"

    // Error messages
    const val ERROR_LOADING_TODAY = "Error loading today's tasks: "
    const val ERROR_LOADING_UPCOMING = "Error loading upcoming tasks: "
    const val ERROR_LOADING_COMPLETED = "Error loading completed tasks: "
    const val ERROR_LOADING_DETAILS = "Error loading task details: "
    const val ERROR_UPDATING_TASK = "Error updating task: "
    const val ERROR_DELETING_TASK = "Error deleting task: "
    const val ERROR_RESTORING_TASK = "Error restoring task: "
    const val ERROR_SAVING_TASK = "Error saving task: "
    const val TASK_NOT_FOUND = "Task not found"

    // Date & Time
    const val TODAY = "Today"
    const val TOMORROW = "Tomorrow"
    const val YESTERDAY = "Yesterday"
    const val LAST_7_DAYS = "Last 7 Days"
    const val LAST_30_DAYS = "Last 30 Days"
    const val EARLIER_THIS_YEAR = "Earlier This Year"
    const val OLDER = "Older"
    const val MONTH_UNKNOWN = "Unknown"
    const val JAN = "Jan"
    const val FEB = "Feb"
    const val MAR = "Mar"
    const val APR = "Apr"
    const val MAY = "May"
    const val JUN = "Jun"
    const val JUL = "Jul"
    const val AUG = "Aug"
    const val SEP = "Sep"
    const val OCT = "Oct"
    const val NOV = "Nov"
    const val DEC = "Dec"
    const val AM = "AM"
    const val PM = "PM"

    // Notification constants
    const val NOTIFICATION_CHANNEL_ID = "todo_reminders"
    const val NOTIFICATION_CHANNEL_NAME = "Todo Reminders"
    const val NOTIFICATION_CHANNEL_DESCRIPTION = "Notifications for todo reminders"
    const val EXTRA_TODO_ID = "todo_id"
    const val EXTRA_TODO_TITLE = "todo_title"
    const val EXTRA_TODO_DESCRIPTION = "todo_description"
    const val EXTRA_TODO_DUE_DATE = "todo_due_date"
    const val REMINDER_FOR_TASK = "Reminder for your task"
    const val NOTIFICATION_PERMISSION_REQUEST_CODE = 100
    const val NOTIFICATION_ID_PREFIX = "todo_"
    const val NOTIFICATION_IMMEDIATE_PREFIX = "todo_immediate_"
    const val CANNOT_SHOW_NOTIFICATION =
        "Cannot show notification - missing notification permission"
}