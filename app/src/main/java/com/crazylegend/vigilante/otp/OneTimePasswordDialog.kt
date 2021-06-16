package com.crazylegend.vigilante.otp

import android.os.Bundle
import android.view.View
import com.crazylegend.kotlinextensions.context.copyToClipboard
import com.crazylegend.kotlinextensions.insets.hideKeyboard
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.toaster.Toaster
import com.crazylegend.kotlinextensions.views.*
import com.crazylegend.security.generateOneTimePassword
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractBottomSheet
import com.crazylegend.vigilante.databinding.DialogOneTimePasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by funkymuse on 6/13/21 to long live and prosper !
 */
@AndroidEntryPoint
class OneTimePasswordDialog : AbstractBottomSheet<DialogOneTimePasswordBinding>() {

    private companion object {
        private const val PASSWORD_STATE = "isPasswordShownState"
    }

    override val viewRes: Int
        get() = R.layout.dialog_one_time_password

    @Inject
    lateinit var toaster: Toaster

    override val binding by viewBinding(DialogOneTimePasswordBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.generatePassword.setOnClickListenerCooldown {
            validatePasswordLength()
        }

        binding.password.setOnClickListenerCooldown {
            requireContext().copyToClipboard(binding.password.textString)
            toaster.shortToast(R.string.content_copied_to_clipboard)
        }
        binding.inputPasswordLength.onImeAction2 { _, _ ->
            validatePasswordLength()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PASSWORD_STATE, binding.password.textString)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        restorePassword(savedInstanceState)
    }

    private fun restorePassword(savedInstanceState: Bundle?) {
        savedInstanceState?.apply {
            getString(PASSWORD_STATE, null).let { pass ->
                if (pass.isNotNullOrEmpty()) {
                    binding.password.text = pass
                    binding.password.visible()
                }
            }
        }
    }

    private fun validatePasswordLength(passwordLength: Int? = binding.inputPasswordLength.textString.toIntOrNull()) {
        binding.inputPasswordLength.clearError()

        when {
            passwordLength == null -> {
                binding.inputPasswordLength.error = getString(R.string.password_too_short)
                return
            }
            passwordLength < 8 -> {
                binding.inputPasswordLength.error = getString(R.string.password_too_short)
                return
            }
            else -> {
                binding.password.apply {
                    visible()
                    text = generateOneTimePassword(passwordLength, binding.shuffleCharactersCheckBox.isChecked)
                    clearFocus()
                    hideSoftInput()
                    hideKeyboard()
                }
            }
        }
    }
}