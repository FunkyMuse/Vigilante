package com.crazylegend.vigilante.permissions

import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.toggle
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 10/21/20 to long live and prosper !
 */
@ServiceScoped
class PermissionsProcessor @Inject constructor(
        @ServiceContext private val context: Context) {

    private var permissionMessage: String? = null

    private var denyButtonId: String? = null
    private var allowButtonId: String? = null
    private var doNotAskButtonId: String? = null
    private var onlyForegroundButtonId: String? = null
    private var alwaysAllowButtonId: String? = null
    private var allowOneTimeButtonId: String? = null
    private var denyAndDoNotAskAgainButtonId: String? = null
    private var denyAndDoNotAskAgainCheckboxButtonId: String? = null
    private var clickedDenyAndDoNotAskAgainCheckBox = false

    //from settings
    private var allowSettingsButtonId: String? = null
    private var permissionsFromSettings = false
    private var alwaysAllowSettingsButtonId: String? = null
    private var allowForegroundOnlySettingsButtonId: String? = null
    private var askEveryTimeSettingsButtonId: String? = null
    private var denySettingsButtonId: String? = null

    fun extractPermission(nodeInfo: AccessibilityNodeInfo?, depth: Int = 0) {
        if (nodeInfo == null) return
        val newPermissionMessage = nodeInfo.getTextForViewId(
                "com.android.permissioncontroller:id/permission_message",
                "com.android.packageinstaller:id/permission_message")

        if (newPermissionMessage.isNotNullOrEmpty()) {
            if (!newPermissionMessage.equals(permissionMessage, true)) {
                //send notification
                debug { "SEND PERMISSION NOTIFICATION $newPermissionMessage" }

            }
            permissionMessage = newPermissionMessage
        }


        nodeInfo.onResourceFound("com.android.permissioncontroller:id/app_permission_root") {
            permissionsFromSettings = true
        }


        nodeInfo.onResourceFound("com.android.permissioncontroller:id/allow_always_radio_button") {
            alwaysAllowSettingsButtonId = it
        }

        nodeInfo.onResourceFound("com.android.packageinstaller:id/permission_allow_button") {
            allowButtonId = it
        }

        nodeInfo.onResourceFound("com.android.permissioncontroller:id/allow_radio_button") {
            allowSettingsButtonId = it
        }

        nodeInfo.onResourceFound("com.android.permissioncontroller:id/allow_foreground_only_radio_button",
                "com.android.permissioncontroller:id/foreground_only_radio_button") {
            allowForegroundOnlySettingsButtonId = it
        }

        nodeInfo.onResourceFound("com.android.permissioncontroller:id/ask_radio_button") {
            askEveryTimeSettingsButtonId = it
        }

        nodeInfo.onResourceFound("com.android.permissioncontroller:id/deny_radio_button") {
            denySettingsButtonId = it
        }

        nodeInfo.onResourceFound("com.android.permissioncontroller:id/permission_allow_foreground_only_button") {
            onlyForegroundButtonId = it
        }

        nodeInfo.onResourceFound("com.android.permissioncontroller:id/permission_deny_button",
                "com.android.packageinstaller:id/permission_deny_button") {
            denyButtonId = it
        }

        nodeInfo.onResourceFound("com.android.permissioncontroller:id/permission_allow_always_button") {
            alwaysAllowButtonId = it
        }

        nodeInfo.onResourceFound("com.android.permissioncontroller:id/permission_allow_one_time_button") {
            allowOneTimeButtonId = it
        }

        nodeInfo.onResourceFound("com.android.permissioncontroller:id/permission_deny_and_dont_ask_again_button") {
            denyAndDoNotAskAgainButtonId = it
        }

        nodeInfo.onResourceFound(
                "com.android.packageinstaller:id/do_not_ask_checkbox") {
            denyAndDoNotAskAgainCheckboxButtonId = it
        }


        for (i in 0 until nodeInfo.childCount) {
            extractPermission(nodeInfo.getChild(i), depth + 1)
        }
    }

    fun listenForPermissionClicks(eventType: Int, source: AccessibilityNodeInfo?) {
        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED && source != null) {
            checkOnClick(denyButtonId, source) {
                if (clickedDenyAndDoNotAskAgainCheckBox) {
                    debug { "CLICKED DENY AND DO NOT ASK AGAIN" }
                    clickedDenyAndDoNotAskAgainCheckBox = false
                }
                denyButtonId = null
            }
            checkOnClick(allowButtonId, source) {
                allowButtonId = null
            }
            checkOnClick(doNotAskButtonId, source) {
                doNotAskButtonId = null
            }
            checkOnClick(onlyForegroundButtonId, source) {
                onlyForegroundButtonId = null
            }
            checkOnClick(alwaysAllowButtonId, source) {
                alwaysAllowButtonId = null
            }

            checkOnClick(allowOneTimeButtonId, source) {
                allowOneTimeButtonId = null
            }

            checkOnClick(denyAndDoNotAskAgainButtonId, source) {
                denyAndDoNotAskAgainButtonId = null
            }

            checkOnClick(denyAndDoNotAskAgainCheckboxButtonId, source) {
                clickedDenyAndDoNotAskAgainCheckBox = clickedDenyAndDoNotAskAgainCheckBox.toggle()
                denyAndDoNotAskAgainCheckboxButtonId = null
            }

            if (permissionsFromSettings) {
                checkOnClick(alwaysAllowSettingsButtonId, source) {
                    alwaysAllowSettingsButtonId = null
                }

                checkOnClick(allowSettingsButtonId, source) {
                    allowSettingsButtonId = null
                }

                checkOnClick(allowForegroundOnlySettingsButtonId, source) {
                    allowForegroundOnlySettingsButtonId = null
                }

                checkOnClick(askEveryTimeSettingsButtonId, source) {
                    askEveryTimeSettingsButtonId = null
                }

                checkOnClick(denySettingsButtonId, source) {
                    denySettingsButtonId = null
                }
            }
        }
    }

    private inline fun checkOnClick(buttonId: String?, source: AccessibilityNodeInfo, onClickFunction: () -> Unit) {
        if (buttonId != null && source.viewIdResourceName != null && source.viewIdResourceName == buttonId) {
            onClickFunction()
            permissionMessage = null
        }
    }

    private fun AccessibilityNodeInfo?.getTextForViewId(comparator: String): String? =
            if (this != null && viewIdResourceName?.contains(comparator, true) == true) {
                text?.toString()
            } else {
                null
            }

    private inline fun AccessibilityNodeInfo?.onResourceFound(comparator: String, onResourceFound: (buttonID: String) -> Unit) = run {
        if (this != null && viewIdResourceName?.contains(comparator, true) == true) {
            onResourceFound(viewIdResourceName)
        }
    }

    private inline fun AccessibilityNodeInfo?.onResourceFound(comparator1: String, comparator2: String, onResourceFound: (buttonID: String) -> Unit) = run {
        if (this != null) {
            when {
                viewIdResourceName?.contains(comparator1, true) == true -> {
                    onResourceFound(viewIdResourceName)
                }
                viewIdResourceName?.contains(comparator2, true) == true -> {
                    onResourceFound(viewIdResourceName)
                }
            }
        }
    }

    private fun AccessibilityNodeInfo?.getTextForViewId(comparator1: String, comparator2: String): String? =
            if (this != null) {
                when {
                    viewIdResourceName?.contains(comparator1, true) == true -> text?.toString()
                    viewIdResourceName?.contains(comparator2, true) == true -> text?.toString()
                    else -> null
                }
            } else {
                null
            }

}

