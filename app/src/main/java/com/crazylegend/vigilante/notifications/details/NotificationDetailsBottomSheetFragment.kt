package com.crazylegend.vigilante.notifications.details

import android.app.Notification
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.navArgs
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.database.DBResult
import com.crazylegend.database.handle
import com.crazylegend.kotlinextensions.color.toHexString
import com.crazylegend.kotlinextensions.context.copyToClipboard
import com.crazylegend.kotlinextensions.context.getAppIcon
import com.crazylegend.kotlinextensions.context.getAppName
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.tryOrElse
import com.crazylegend.kotlinextensions.tryOrNull
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.kotlinextensions.views.visibleIfTrueGoneOtherwise
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractBottomSheet
import com.crazylegend.vigilante.databinding.DialogNotificationDetailsBinding
import com.crazylegend.vigilante.di.providers.prefs.defaultPrefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.notifications.db.NotificationsModel
import com.crazylegend.vigilante.utils.assistedViewModel
import com.crazylegend.vigilante.utils.onStartedRepeatingAction
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
@AndroidEntryPoint
class NotificationDetailsBottomSheetFragment : AbstractBottomSheet<DialogNotificationDetailsBinding>() {

    @Inject
    lateinit var prefsProvider: DefaultPreferencessProvider


    override val viewRes: Int
        get() = R.layout.dialog_notification_details

    override val binding by viewBinding(DialogNotificationDetailsBinding::bind)

    private val args by navArgs<NotificationDetailsBottomSheetFragmentArgs>()

    @Inject
    lateinit var notificationDetailsVMFactory: NotificationDetailsViewModel.NotificationDetailsVMFactory

    private val notificationDetailsVM by assistedViewModel {
        notificationDetailsVMFactory.create(args.notificationID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onStartedRepeatingAction {
            notificationDetailsVM.notification.collectLatest {
                handleDatabaseResult(it)
            }
        }
    }

    private fun handleDatabaseResult(dbResult: DBResult<NotificationsModel>) {
        binding.loading.visibleIfTrueGoneOtherwise(dbResult is DBResult.Querying)
        dbResult.handle(
                dbError = {
                    CrashyReporter.logException(it)
                    dismissAllowingStateLoss()
                    shortToast(R.string.error_occurred)
                },
                success = {
                    updateUI(this)
                }
        )
    }

    private fun updateUI(model: NotificationsModel) {
        model.apply {
            title?.apply { generateTextHolder(R.string.title_placeholder, this) }
            bigText?.apply { generateTextHolder(R.string.big_text_placeholder, this) }
            text?.apply { generateTextHolder(R.string.text_placeholder, this) }
            category?.apply { generateTextHolder(R.string.category_placeholder, this) }
            group?.apply { generateTextHolder(R.string.group_placeholder, this) }
            channelId?.apply { generateTextHolder(R.string.channel_id_placeholder, this) }
            sentByPackage?.apply {
                binding.appIcon.setImageDrawable(tryOrNull { requireContext().getAppIcon(this) })
                binding.appName.text = tryOrElse(getString(R.string.app_not_installed)) { requireContext().getAppName(this) }
            }
            generateTextHolder(R.string.date_placeholder, showTime.toString(prefsProvider.getDateFormat))

            when (visibility) {
                Notification.VISIBILITY_PUBLIC -> R.string.notification_visibility_public
                Notification.VISIBILITY_PRIVATE -> R.string.notification_visibility_private
                Notification.VISIBILITY_SECRET -> R.string.notification_visibility_secret
                else -> null
            }?.apply {
                generateTextHolder(this, getString(this))
            }

            color?.apply {
                val hexColor = toHexString()
                generateTextHolder(R.string.notification_color_placeholder, hexColor).also {
                    it.setTextColor(Color.parseColor(hexColor))
                }
            }
        }
    }

    private fun generateTextHolder(@StringRes stringRes: Int, content: String): MaterialTextView {
        val textView = MaterialTextView(requireContext()).apply {
            setPadding(22)
            text = getString(stringRes, content)
        }
        binding.textHolder.addView(textView)
        textView.updateLayoutParams<LinearLayout.LayoutParams> {
            marginStart = 12
            marginEnd = 12
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        binding.textHolder.setOnClickListenerCooldown {
            requireContext().copyToClipboard(content)
            shortToast(R.string.content_copied_to_clipboard)
        }
        return textView
    }
}