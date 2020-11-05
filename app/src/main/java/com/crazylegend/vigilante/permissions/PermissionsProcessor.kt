package com.crazylegend.vigilante.permissions

import android.annotation.SuppressLint
import android.util.ArrayMap
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.coroutines.makeIOCall
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.toggle
import com.crazylegend.vigilante.contracts.service.ServiceManagersCoroutines
import com.crazylegend.vigilante.permissions.clicks.PermissionClickModel
import com.crazylegend.vigilante.permissions.requests.db.PermissionRequestModel
import com.crazylegend.vigilante.permissions.requests.db.PermissionRequestsRepository
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by crazy on 10/21/20 to long live and prosper !
 */
@ServiceScoped
@SuppressLint("DefaultLocale")
class PermissionsProcessor @Inject constructor(
        private val permissionRequestsRepository: PermissionRequestsRepository
) : ServiceManagersCoroutines {

    override val serviceLifecycleDispatcher: ServiceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    override fun initVars() {}

    override fun registerCallbacks() {
        scope.launch {
            permissionFlow.collectLatest {
                sendNotification(it)
            }
        }
    }

    private fun sendNotification(newPermissionMessage: String) {
        if (newPermissionMessage.isNotBlank()) {
            val currentPackageRef = packageRequestingPermission
            val permissionRequestModel = PermissionRequestModel(newPermissionMessage, currentPackageRef)
            scope.makeIOCall {
                permissionRequestsRepository.insertPermissionRequest(permissionRequestModel)
            }
        }
    }

    override fun disposeResources() {}

    private val dismissPackages = setOf(
            "com.google.android.permissioncontroller",
            "com.android.systemui",
            "com.google.android.packageinstaller",
            "com.android.packageinstaller",
    )

    override fun eventActionByPackageName(eventPackageName: CharSequence) {
        if (eventPackageName !in dismissPackages) {
            debug { "CURRENT PACKAGE STRING ${eventPackageName.toString()}" }
            packageRequestingPermission = eventPackageName.toString()
        }
    }

    private var permissionMessage: String? = null
    private var packageRequestingPermission: String? = null
    private var clickedDenyAndDoNotAskAgainCheckBox = false
    private val permissionFlow = MutableStateFlow("")
    private val buttonsMap = ArrayMap<String, String>()

    fun extractPermissionMessage(nodeInfo: AccessibilityNodeInfo?, depth: Int = 0) {
        if (nodeInfo == null) return
        val newPermissionMessage = nodeInfo.getTextForViewId(
                "com.android.permissioncontroller:id/permission_message",
                "com.android.packageinstaller:id/permission_message")

        if (newPermissionMessage.isNotNullOrEmpty()) {
            permissionMessage = newPermissionMessage
            scope.launch {
                permissionFlow.emit(newPermissionMessage ?: "")
            }
        } else {
            for (i in 0 until nodeInfo.childCount) {
                extractPermissionMessage(nodeInfo.getChild(i), depth + 1)
            }
        }
    }

    fun extractPermissionButtons(nodeInfo: AccessibilityNodeInfo?, depth: Int = 0) {
        if (nodeInfo == null) return

        nodeInfo.findButtons(PermissionType.values().map { it.res.toLowerCase() })

        for (i in 0 until nodeInfo.childCount) {
            extractPermissionButtons(nodeInfo.getChild(i), depth + 1)
        }
    }

    fun listenForPermissionClicks(eventType: Int, source: AccessibilityNodeInfo?) {
        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED && source != null) {
            checkOnClick(source)
        }
    }

    private fun AccessibilityNodeInfo?.findButtons(list: List<String>) {
        if (this != null) {
            list.find { viewIdResourceName?.toLowerCase() == it }?.apply {
                buttonsMap[this.toLowerCase()] = this.toLowerCase()
            }
        }
    }

    private val allowedPermissionTypes = arrayOf(
            PermissionType.ALLOW,
            PermissionType.ALLOW_FOREGROUND_ONLY,
            PermissionType.ALLOW_FOREGROUND_ONLY,
            PermissionType.ALWAYS_ALLOW,
            PermissionType.ALLOW_ONE_TIME,
            PermissionType.ALWAYS_ALLOW_SETTINGS,
            PermissionType.ALLOW_SETTINGS,
            PermissionType.ALLOW_FOREGROUND_ONLY_SETTINGS,
            PermissionType.ALLOW_FOREGROUND_ONLY_SETTINGS2,
    )

    /**
     * This implementation doesn't work on all devices, LG and their shitty accessibility information
     * my LG G4 cries in pieces after discovering LG V20 doesn't handle this at all, shame
     * @param source AccessibilityNodeInfo
     */
    private fun checkOnClick(source: AccessibilityNodeInfo) {
        if (source.viewIdResourceName != null) {
            buttonsMap[source.viewIdResourceName.toLowerCase()]?.let { clickedButtonID ->
                if (clickedButtonID == PermissionType.DENY_DO_NOT_ASK_AGAIN_CHECKBOX.res) {
                    clickedDenyAndDoNotAskAgainCheckBox = clickedDenyAndDoNotAskAgainCheckBox.toggle()
                }
                val hasUserClockedDenyAndDoNotAskAgainWhenDenying = if (clickedButtonID == PermissionType.DENY.res) {
                    if (clickedDenyAndDoNotAskAgainCheckBox) {
                        clickedDenyAndDoNotAskAgainCheckBox = false
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
                val permissionClickModel = PermissionClickModel(
                        packageRequestingPermission, permissionMessage,
                        permissionClickType = clickedButtonID)
                debug { "CLICKED on $permissionClickModel" }
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




