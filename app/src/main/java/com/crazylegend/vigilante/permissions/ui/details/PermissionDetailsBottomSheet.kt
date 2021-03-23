package com.crazylegend.vigilante.permissions.ui.details

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.context.getAppIcon
import com.crazylegend.kotlinextensions.context.getAppName
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.fragments.viewCoroutineScope
import com.crazylegend.kotlinextensions.tryOrNull
import com.crazylegend.kotlinextensions.views.setTextAndShowOrGone
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractBottomSheet
import com.crazylegend.vigilante.databinding.DialogPermissionDetailsBinding
import com.crazylegend.vigilante.utils.assistedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject

/**
 * Created by crazy on 11/6/20 to long live and prosper !
 */

@AndroidEntryPoint
class PermissionDetailsBottomSheet : AbstractBottomSheet<DialogPermissionDetailsBinding>() {

    override val binding by viewBinding(DialogPermissionDetailsBinding::bind)
    override val viewRes: Int
        get() = R.layout.dialog_permission_details

    private val args by navArgs<PermissionDetailsBottomSheetArgs>()
    private val packageName get() = args.packageName
    private val date get() = Date(args.date)
    private val permissionMessage get() = args.permissionMessage
    private val settingsAppName get() = args.settingsAppName

    @Inject
    lateinit var permissionDetailsVMFactory: PermissionDetailsViewModel.PermissionDetailsVMFactory
    private val permissionDetailsVM by assistedViewModel {
        permissionDetailsVMFactory.create(packageName)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewCoroutineScope.launchWhenResumed {
            permissionDetailsVM.permissionRequests.collectLatest {
                binding.appPermissionRequestCount.text.text = getString(R.string.permissions_requests_by_app_count, it)
            }
        }

        binding.appIcon.setImageDrawable(tryOrNull { requireContext().getAppIcon(packageName) })
        binding.appName.text = tryOrNull { requireContext().getAppName(packageName) }
        binding.date.text.text = date.toString(prefsProvider.getDateFormat)
        binding.date.image.setImageResource(R.drawable.ic_calendar)
        if (permissionMessage?.endsWith("?", true) == false) {
            binding.appNameFromSettings.setTextAndShowOrGone(settingsAppName)
        }
        binding.appPermissionRequestCount.image.setImageResource(R.drawable.security)
        binding.permissionMessage.text = permissionMessage
    }
}