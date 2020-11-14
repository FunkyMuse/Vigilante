package com.crazylegend.vigilante.di.providers

import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import com.crazylegend.biometrics.biometricAuth
import com.crazylegend.vigilante.di.qualifiers.FragmentContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/14/20 to long live and prosper !
 */
@FragmentScoped
class AuthProvider @Inject constructor(private val fragment: Fragment,
                                       @FragmentContext private val context: Context) {

    val canAuthenticate = BiometricManager.from(context).canAuthenticate(authType) == BiometricManager.BIOMETRIC_SUCCESS

    private val authType
        get() = when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.R -> {
                BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            }
            else -> BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        }

    fun confirmBiometricAuth(
            @StringRes title: Int,
            @StringRes description: Int,
            onAuthFailed: () -> Unit,
            onAuthError: (errorCode: Int, errorMessage: String) -> Unit = { _, _ -> },
            onAuthSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit = { _ -> }) {
        fragment.biometricAuth(promptInfoAction = {
            setTitle(fragment.getString(title))
            setDescription(fragment.getString(description))
            setAllowedAuthenticators(authType)
            this
        }, onAuthFailed, onAuthError, onAuthSuccess)
    }


}