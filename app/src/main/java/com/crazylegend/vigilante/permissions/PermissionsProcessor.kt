package com.crazylegend.vigilante.permissions

import android.annotation.SuppressLint
import android.view.accessibility.AccessibilityNodeInfo
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.coroutines.makeIOCall
import com.crazylegend.string.isNotNullOrEmpty
import com.crazylegend.vigilante.contracts.service.ServiceLifecycle
import com.crazylegend.vigilante.permissions.db.PermissionRequestModel
import com.crazylegend.vigilante.permissions.db.PermissionRequestsDAO
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

/**
 * Created by crazy on 10/21/20 to long live and prosper !
 */
@ServiceScoped
@SuppressLint("DefaultLocale")
class PermissionsProcessor @Inject constructor(
        private val permissionRequestsRepository: PermissionRequestsDAO,
) : ServiceLifecycle {

    override val serviceLifecycleDispatcher: ServiceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    override fun initVars() {
        settingsPermissionTitle = AtomicReference(null)
    }

    override fun registerCallbacks() {
        scope.launch {
            permissionFlow.collectLatest {
                savePermissionMessage(it)
            }
        }
    }

    private fun savePermissionMessage(newPermissionMessage: String) {
        if (newPermissionMessage.isNotBlank()) {
            val currentPackageRef = packageRequestingPermission
            val settingsTitle = settingsPermissionTitle.getAndSet(null)
            val permissionRequestModel = PermissionRequestModel(newPermissionMessage, currentPackageRef, settingsAppName = settingsTitle)
            settingsPermissionTitle.set(null)
            scope.makeIOCall {
                permissionRequestsRepository.insertPermissionRequest(permissionRequestModel)
            }
        }
    }

    override fun disposeResources() {
        permissionMessage = null
        packageRequestingPermission = null
    }


    fun eventActionByPackageName(eventPackageName: CharSequence) {
        packageRequestingPermission = eventPackageName.toString()
    }

    private var permissionMessage: String? = null
    private lateinit var settingsPermissionTitle: AtomicReference<String?>
    private var packageRequestingPermission: String? = null
    private val permissionFlow = MutableStateFlow("")

    fun extractPermissionMessage(nodeInfo: AccessibilityNodeInfo?) {
        if (nodeInfo == null) return
        val newPermissionMessage = nodeInfo.getTextForViewId(
                "com.android.permissioncontroller:id/permission_message",
                "com.android.packageinstaller:id/permission_message")

        val settingsText = nodeInfo.getTextForViewId("com.android.permissioncontroller:id/entity_header_title")
        if (settingsText != null) {
            settingsPermissionTitle.set(settingsText)
        }
        if (newPermissionMessage.isNotNullOrEmpty()) {
            permissionMessage = newPermissionMessage
            scope.launch {
                permissionFlow.emit(newPermissionMessage ?: "")
            }
        } else {
            for (i in 0 until nodeInfo.childCount) {
                extractPermissionMessage(nodeInfo.getChild(i))
            }
        }
    }

    private fun AccessibilityNodeInfo?.getTextForViewId(comparator1: String, comparator2: String? = null): String? =
            if (this != null) {
                when {
                    viewIdResourceName?.contains(comparator1, true) == true -> text?.toString()
                    comparator2?.let { viewIdResourceName?.contains(it, true) } == true -> text?.toString()
                    else -> null
                }
            } else {
                null
            }
}




