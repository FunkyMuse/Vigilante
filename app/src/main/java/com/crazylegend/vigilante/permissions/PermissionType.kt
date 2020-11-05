package com.crazylegend.vigilante.permissions

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
enum class PermissionType(val res: String) {
    ALLOW("com.android.packageinstaller:id/permission_allow_button"),
    ALLOW_FOREGROUND_ONLY("com.android.permissioncontroller:id/permission_allow_foreground_only_button"),
    ALWAYS_ALLOW("com.android.permissioncontroller:id/permission_allow_always_button"),
    ALLOW_ONE_TIME("com.android.permissioncontroller:id/permission_allow_one_time_button"),

    DENY("com.android.permissioncontroller:id/permission_deny_button"),
    DENY2("com.android.packageinstaller:id/permission_deny_button"),

    DENY_DO_NOT_ASK_AGAIN("com.android.permissioncontroller:id/permission_deny_and_dont_ask_again_button"),
    DENY_DO_NOT_ASK_AGAIN_CHECKBOX("com.android.packageinstaller:id/do_not_ask_checkbox"),


    ALWAYS_ALLOW_SETTINGS("com.android.permissioncontroller:id/allow_always_radio_button"),
    ALLOW_SETTINGS("com.android.permissioncontroller:id/allow_radio_button"),

    ALLOW_FOREGROUND_ONLY_SETTINGS("com.android.permissioncontroller:id/allow_foreground_only_radio_button"),
    ALLOW_FOREGROUND_ONLY_SETTINGS2("com.android.permissioncontroller:id/foreground_only_radio_button"),

    DENY_SETTINGS("com.android.permissioncontroller:id/deny_radio_button"),

    ASK_EVERY_TIME_SETTINGS("com.android.permissioncontroller:id/ask_radio_button")
}