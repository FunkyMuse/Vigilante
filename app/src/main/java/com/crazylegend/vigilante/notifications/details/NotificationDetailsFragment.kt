package com.crazylegend.vigilante.notifications.details

import android.app.Notification
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
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
import com.crazylegend.kotlinextensions.fragments.viewCoroutineScope
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.kotlinextensions.views.visibleIfTrueGoneOtherwise
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractBottomSheet
import com.crazylegend.vigilante.databinding.DialogNotificationDetailsBinding
import com.crazylegend.vigilante.notifications.db.NotificationsModel
import com.crazylegend.vigilante.utils.assistedViewModel
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
@AndroidEntryPoint
class NotificationDetailsFragment : AbstractBottomSheet<DialogNotificationDetailsBinding>() {

    override val viewRes: Int
        get() = R.layout.dialog_notification_details

    override val binding by viewBinding(DialogNotificationDetailsBinding::bind)

    private val args by navArgs<NotificationDetailsFragmentArgs>()

    @Inject
    lateinit var notificationDetailsVMFactory: NotificationDetailsVM.NotificationDetailsVMFactory

    private val notificationDetailsVM by assistedViewModel {
        notificationDetailsVMFactory.create(args.notificationID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewCoroutineScope.launchWhenResumed {
            notificationDetailsVM.notification.collectLatest {
                binding.loading.visibleIfTrueGoneOtherwise(it is DBResult.Querying)
                it.handle(
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
        }
    }

    private fun updateUI(model: NotificationsModel) {
        model.apply {
            title?.apply {
                generateTextHolder(getString(R.string.title_placeholder, this))
            }
            bigText?.apply {
                generateTextHolder(getString(R.string.big_text_placeholder, this))
            }

            text?.apply {
                generateTextHolder(getString(R.string.text_placeholder, this))
            }

            category?.apply {
                generateTextHolder(getString(R.string.category_placeholder, this))
            }
            group?.apply {
                generateTextHolder(getString(R.string.group_placeholder, this))
            }

            channelId?.apply {
                generateTextHolder(getString(R.string.channel_id_placeholder, this))
            }
            sentByPackage?.apply {
                binding.appIcon.setImageDrawable(requireContext().getAppIcon(this))
                binding.appName.text = requireContext().getAppName(this)
            }
            generateTextHolder(getString(R.string.date_placeholder, showTime.toString(prefsProvider.getDateFormat)))

            when (visibility) {
                Notification.VISIBILITY_PUBLIC -> R.string.notification_visibility_public
                Notification.VISIBILITY_PRIVATE -> R.string.notification_visibility_private
                Notification.VISIBILITY_SECRET -> R.string.notification_visibility_secret
                else -> null
            }?.apply {
                generateTextHolder(getString(this))
            }

            color?.apply {
                val hexColor = toHexString()
                generateTextHolder(getString(R.string.notification_color_placeholder, hexColor)).also {
                    it.setOnClickListenerCooldown {
                        requireContext().copyToClipboard(hexColor)
                        shortToast(R.string.notification_color_copied_to_clipboard)
                    }
                    it.setTextColor(Color.parseColor(hexColor))
                }
            }
        }
    }

    private fun generateTextHolder(withText: String): MaterialTextView {
        val textView = MaterialTextView(requireContext()).apply {
            setPadding(22)
            text = withText
        }
        binding.textHolder.addView(textView)
        textView.updateLayoutParams<LinearLayout.LayoutParams> {
            marginStart = 12
            marginEnd = 12
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        return textView
    }
}