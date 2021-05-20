package com.crazylegend.vigilante.customization

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.kotlinextensions.effects.vibrate
import com.crazylegend.kotlinextensions.fragments.finish
import com.crazylegend.kotlinextensions.fragments.fragmentBooleanResult
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.views.*
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.confirmation.DialogConfirmation
import com.crazylegend.vigilante.databinding.FragmentCustomizationBinding
import com.crazylegend.vigilante.di.providers.prefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.di.providers.prefs.DefaultPreferencessProvider.Companion.DEFAULT_SPACING
import com.crazylegend.vigilante.home.HomeFragmentDirections
import com.crazylegend.vigilante.service.VigilanteService
import com.crazylegend.vigilante.settings.CAMERA_CUSTOMIZATION_BASE_PREF
import com.crazylegend.vigilante.utils.goToScreen
import com.crazylegend.vigilante.utils.uiAction
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Created by crazy on 11/8/20 to long live and prosper !
 */
@AndroidEntryPoint
class CustomizationFragment : AbstractFragment<FragmentCustomizationBinding>(R.layout.fragment_customization) {


    companion object {
        private const val COLOR_DOT_STATE = "colorState"
        private const val LAYOUT_STATE = "layoutState"
        private const val SIZE_STATE = "sizeState"
        private const val COLOR_NOTIFICATION_STATE = "colorNotificationState"
        private const val VIBRATION_STATE = "vibrationState"
        private const val SPACING_STATE = "customSpacingState"

        const val COLOR_DOT_PREF_ADDITION = "colorChoice"
        const val COLOR_NOTIFICATION_PREF_ADDITION = "colorChoiceNotification"
        const val SIZE_PREF_ADDITION = "sizeChoice"
        const val POSITION_PREF_ADDITION = "positionChoice"
        const val POSITION_SPACING_ADDITION = "spacingChoice"
        const val VIBRATION_PREF_ADDITION = "vibrationChoice"
    }

    @Inject
    lateinit var prefsProvider: DefaultPreferencessProvider

    override val binding by viewBinding(FragmentCustomizationBinding::bind)

    private val defaultDotColor get() = if (prefBaseName == CAMERA_CUSTOMIZATION_BASE_PREF) prefsProvider.getCameraColorPref else prefsProvider.getMicColorPref
    private val defaultNotificationLEDColor get() = if (prefBaseName == CAMERA_CUSTOMIZATION_BASE_PREF) prefsProvider.getCameraColorPref else prefsProvider.getMicColorPref
    private val defaultSize get() = if (prefBaseName == CAMERA_CUSTOMIZATION_BASE_PREF) prefsProvider.getCameraSizePref else prefsProvider.getMicSizePref
    private val defaultLayoutPosition get() = if (prefBaseName == CAMERA_CUSTOMIZATION_BASE_PREF) prefsProvider.getCameraPositionPref else prefsProvider.getMicPositionPref
    private val defaultVibrationPosition get() = if (prefBaseName == CAMERA_CUSTOMIZATION_BASE_PREF) prefsProvider.getCameraVibrationPositionPref else prefsProvider.getMicVibrationPositionPref
    private val spacing get() = if (prefBaseName == CAMERA_CUSTOMIZATION_BASE_PREF) prefsProvider.getCameraSpacing else prefsProvider.getMicSpacing
    private val title get() = if (prefBaseName == CAMERA_CUSTOMIZATION_BASE_PREF) getString(R.string.camera_title) else getString(R.string.microphone_title)

    private var pickedDotColor: Int? = null
    private var pickedNotificationLEDColor: Int? = null
    private var pickedSize: Float? = null
    private var pickedLayoutPosition: Int? = null
    private var pickedSpacing: Int? = null
    private var pickedVibrationPosition: Int? = null

    private val navArgs by navArgs<CustomizationFragmentArgs>()
    private val prefBaseName get() = navArgs.prefBaseName


    private fun updatePreviewColor(newValue: Int?) {
        newValue ?: return
        binding.preview.setColorFilter(newValue)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (prefBaseName.isEmpty()) {
            throwMissingArgException()
        }

        //region spacing
        pickedSpacing = spacing
        binding.inputSpacing.setTheText(pickedSpacing.toString())
        binding.title.text = title
        //endregion

        //region color
        pickedDotColor = defaultDotColor
        pickedNotificationLEDColor = defaultNotificationLEDColor
        updatePreviewColor(pickedDotColor)
        binding.colorPick.setOnClickListenerCooldown {
            showColorPicker(requireContext(), R.string.pick_dot_color, COLOR_DOT_PREF_ADDITION, ::setDotColor)
        }
        binding.colorPickNotificationLed.setOnClickListenerCooldown {
            showColorPicker(requireContext(), R.string.pick_notification_LED_color, COLOR_NOTIFICATION_PREF_ADDITION, ::setNotificationLEDColor)
        }
        //endregion

        //region size
        pickedSize = defaultSize
        updatePickedSize(pickedSize)
        binding.sizeSlider.addOnChangeListener { _, value, _ ->
            updatePreviewWidthAndHeight(value)
            prefsProvider.saveSizePref(prefBaseName + SIZE_PREF_ADDITION, value)
        }
        //endregion

        //region layout
        pickedLayoutPosition = defaultLayoutPosition
        updateLayoutPosition(pickedLayoutPosition)
        binding.layoutPosition.onItemSelected { _, _, position, _ ->
            pickedLayoutPosition = position
        }
        //endregion

        //region vibration
        pickedVibrationPosition = defaultVibrationPosition
        updateVibrationPosition(pickedVibrationPosition)

        binding.vibration.onItemSelected { _, _, position, _ ->
            pickedVibrationPosition = position
        }
        binding.vibrationExplanation.setOnClickListenerCooldown {
            val vibrationPosition = pickedVibrationPosition ?: return@setOnClickListenerCooldown
            prefsProvider.getVibrationEffect(vibrationPosition)?.let { longs -> requireContext().vibrate(longs, -1) }
        }
        //endregion

        binding.backButton.root.setOnClickListenerCooldown {
            backButtonClick()
        }

        binding.scroller.setOnScrollChangeListener { _, _, _, _, _ ->
            removeEditTextFocus()
        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
            backButtonClick()
        }

        fragmentBooleanResult(DialogConfirmation.RESULT_KEY, DialogConfirmation.DEFAULT_REQ_KEY, onDenied = {
            findNavController().navigateUp()
        }, onGranted = {
            prefsProvider.saveSizePref(prefBaseName + SIZE_PREF_ADDITION, binding.sizeSlider.value)
            pickedDotColor?.let { prefsProvider.saveColorPref(prefBaseName + COLOR_DOT_PREF_ADDITION, it) }
            pickedNotificationLEDColor?.let { prefsProvider.saveNotificationColorPref(prefBaseName + COLOR_NOTIFICATION_PREF_ADDITION, it) }
            pickedLayoutPosition?.let { prefsProvider.savePositionPref(prefBaseName + POSITION_PREF_ADDITION, it) }
            pickedVibrationPosition?.let { prefsProvider.savePositionPref(prefBaseName + VIBRATION_PREF_ADDITION, it) }
            binding.inputSpacing.textString.toIntOrNull()?.let { prefsProvider.saveSpacing(prefBaseName, it) }
            VigilanteService.serviceParamsListener?.updateForBasePref(prefBaseName)
            goBack()
        })
    }

    private fun backButtonClick() {
        goToScreen(
                HomeFragmentDirections.destinationConfirmation(
                        cancelButtonText = getString(R.string.discard_changes),
                        confirmationButtonText = getString(R.string.save),
                        titleText = getString(R.string.save_progress_expl)
                )
        )
    }

    private fun goBack() {
        uiAction {
            findNavController().navigateUp()
            shortToast(R.string.saved)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pickedDotColor?.let { outState.putInt(COLOR_DOT_STATE, it) }
        pickedLayoutPosition?.let { outState.putInt(LAYOUT_STATE, it) }
        pickedVibrationPosition?.let { outState.putInt(VIBRATION_STATE, it) }
        pickedSize?.let { outState.putFloat(SIZE_STATE, it) }
        pickedNotificationLEDColor?.let { outState.putInt(COLOR_NOTIFICATION_STATE, it) }
        pickedSpacing?.let { outState.putInt(SPACING_STATE, it) }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        pickedDotColor = savedInstanceState?.getInt(COLOR_DOT_STATE, defaultDotColor)
                ?: defaultDotColor
        pickedLayoutPosition = savedInstanceState?.getInt(LAYOUT_STATE, defaultLayoutPosition)
                ?: defaultLayoutPosition
        pickedSize = savedInstanceState?.getFloat(SIZE_STATE, defaultSize) ?: defaultSize
        pickedNotificationLEDColor = savedInstanceState?.getInt(COLOR_NOTIFICATION_STATE, defaultNotificationLEDColor)
        pickedVibrationPosition = savedInstanceState?.getInt(VIBRATION_STATE, defaultVibrationPosition)
                ?: defaultVibrationPosition
        pickedSpacing = savedInstanceState?.getInt(SPACING_STATE, DEFAULT_SPACING) ?: spacing
        binding.inputSpacing.setTheText(pickedSpacing.toString())
        updatePreviewColor(pickedDotColor)
        updatePickedSize(pickedSize)
        updateLayoutPosition(pickedLayoutPosition)
        updateVibrationPosition(pickedVibrationPosition)
    }

    private inline fun showColorPicker(context: Context, @StringRes title: Int, prefAddition: String, crossinline onColorPicked: (ColorEnvelope?.() -> Unit)) {
        ColorPickerDialog.Builder(context).apply {
            setTitle(getString(title))
            setPreferenceName(prefBaseName + prefAddition)
            setPositiveButton(getString(R.string.select), ColorEnvelopeListener { envelope, _ -> envelope.onColorPicked() })
            setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
            setBottomSpace(12)
            show()
        }
    }

    private fun throwMissingArgException() {
        CrashyReporter.logException(IllegalStateException("Argument is missing in customization"))
        finish()
    }

    //region ui updates
    private fun removeEditTextFocus() {
        if (binding.inputSpacing.isFocused)
            binding.inputSpacing.clearFocusAndKeyboard()

        if (binding.inputSpacingLayout.isFocused)
            binding.inputSpacingLayout.clearFocus()
    }

    private fun setNotificationLEDColor(envelope: ColorEnvelope?) {
        envelope ?: return
        pickedNotificationLEDColor = envelope.color
    }

    private fun setDotColor(envelope: ColorEnvelope?) {
        envelope ?: return
        pickedDotColor = envelope.color
        updatePreviewColor(pickedDotColor)
    }

    private fun updatePickedSize(pickedSize: Float?) {
        pickedSize ?: return
        binding.sizeSlider.value = pickedSize
        updatePreviewWidthAndHeight(pickedSize)
    }

    private fun updateVibrationPosition(pickedVibrationPosition: Int?) {
        pickedVibrationPosition ?: return
        binding.vibration.setSelection(pickedVibrationPosition)
    }

    private fun updateLayoutPosition(pickedLayoutPosition: Int?) {
        pickedLayoutPosition ?: return
        binding.layoutPosition.setSelection(pickedLayoutPosition)
    }

    private fun updatePreviewWidthAndHeight(value: Float?) {
        value ?: return
        binding.preview.width(value / 2)
        binding.preview.height(value / 2)
    }
    //endregion
}