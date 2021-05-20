package com.crazylegend.vigilante.confirmation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.fragments.ifIsAttachedAction
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown

import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractDialogFragment
import com.crazylegend.vigilante.databinding.DialogConfirmationBinding


/**
 * Created by crazy on 7/16/20 to long live and prosper !
 */
class DialogConfirmation : AbstractDialogFragment(R.layout.dialog_confirmation) {

    companion object {
        const val RESULT_KEY = "fragmentResultConfirmation"
        const val DEFAULT_REQ_KEY = "dialogConfirmationRequestKey"
        const val CUSTOM_REQ_KEY = "customReqKey"
    }

    override val binding by viewBinding(DialogConfirmationBinding::bind)
    override val dimAmount: Float
        get() = 0.6f
    override val isCancellable: Boolean
        get() = true

    private val args by navArgs<DialogConfirmationArgs>()
    private val explanation get() = args.titleText
    private val confirmationButtonText get() = args.confirmationButtonText
    private val cancelButtonText get() = args.cancelButtonText ?: getString(R.string.cancel)
    private val customCancellationKey get() = args.customCancellationKey
    private val customConfirmationKey get() = args.customConfirmationKey

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.expl.text = explanation
        binding.confirm.text = confirmationButtonText
        binding.cancel.text = cancelButtonText

        binding.cancel.setOnClickListenerCooldown {
            ifIsAttachedAction { addArgs(false, customCancellationKey) }
        }

        binding.confirm.setOnClickListenerCooldown {
            ifIsAttachedAction { addArgs(true, customConfirmationKey) }
        }
    }

    private fun addArgs(confirmation: Boolean, customKey: Int) {
        setFragmentResult(RESULT_KEY, bundleOf(Pair(DEFAULT_REQ_KEY, confirmation), Pair(CUSTOM_REQ_KEY, customKey)))
        findNavController().navigateUp()
    }
}