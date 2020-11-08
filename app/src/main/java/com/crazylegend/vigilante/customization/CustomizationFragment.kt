package com.crazylegend.vigilante.customization

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.coroutines.onMain
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.kotlinextensions.fragments.fragmentBooleanResult
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.views.height
import com.crazylegend.kotlinextensions.views.onItemSelected
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.kotlinextensions.views.width
import com.crazylegend.navigation.navigateSafe
import com.crazylegend.navigation.navigateUpSafe
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.confirmation.DialogConfirmation
import com.crazylegend.vigilante.databinding.FragmentCustomizationBinding
import com.crazylegend.vigilante.home.HomeFragmentDirections
import com.crazylegend.vigilante.settings.CAMERA_CUSTOMIZATION_BASE_PREF
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by crazy on 11/8/20 to long live and prosper !
 */
@AndroidEntryPoint
class CustomizationFragment : AbstractFragment<FragmentCustomizationBinding>(R.layout.fragment_customization) {

    companion object {
        private const val COLOR_STATE = "colorState"
        private const val LAYOUT_STATE = "layoutState"
        private const val SIZE_STATE = "sizeState"

        const val COLOR_PREF_ADDITION = "colorChoice"
        const val SIZE_PREF_ADDITION = "sizeChoice"
        const val POSITION_PREF_ADDITION = "positionChoice"
    }

    override val binding by viewBinding(FragmentCustomizationBinding::bind)

    private val defaultColor get() = if (prefBaseName == CAMERA_CUSTOMIZATION_BASE_PREF) prefsProvider.getCameraColorPref else prefsProvider.getMicColorPref
    private val defaultSize get() = if (prefBaseName == CAMERA_CUSTOMIZATION_BASE_PREF) prefsProvider.getCameraSizePref else prefsProvider.getMicSizePref
    private val defaultLayoutPosition get() = if (prefBaseName == CAMERA_CUSTOMIZATION_BASE_PREF) prefsProvider.getCameraPositionPref else prefsProvider.getMicPositionPref
    private val title get() = if (prefBaseName == CAMERA_CUSTOMIZATION_BASE_PREF) getString(R.string.camera_title) else getString(R.string.microphone_title)

    private var pickedColor: Int? = null
    private var pickedSize: Float? = null
    private var pickedLayoutPosition: Int? = null

    private val navArgs by navArgs<CustomizationFragmentArgs>()
    private val prefBaseName get() = navArgs.prefBaseName

    private fun updatePreviewColor(newValue: Int) {
        binding.preview.setColorFilter(newValue)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (prefBaseName.isEmpty()) {
            CrashyReporter.logException(IllegalStateException("Argument is missing in customization"))
            findNavController().navigateUpSafe()
        }

        binding.title.text = title

        pickedColor = defaultColor
        updatePreviewColor(pickedColor!!)

        pickedSize = defaultSize
        binding.sizeSlider.value = pickedSize!!
        updatePreviewWidthAndHeight(pickedSize!!)

        pickedLayoutPosition = defaultLayoutPosition
        binding.layoutPosition.setSelection(pickedLayoutPosition!!)

        binding.sizeSlider.addOnChangeListener { _, value, _ ->
            updatePreviewWidthAndHeight(value)
            prefsProvider.saveSizePref(prefBaseName + SIZE_PREF_ADDITION, value)
        }

        binding.colorPick.setOnClickListenerCooldown {
            showDialogPicker(requireContext())
        }

        binding.layoutPosition.onItemSelected { _: AdapterView<*>?, _: View?, position: Int?, _: Long? ->
            pickedLayoutPosition = position
        }


        binding.backButton.setOnClickListenerCooldown {
            findNavController().navigateSafe(
                    HomeFragmentDirections.destinationConfirmation(
                            cancelButtonText = getString(R.string.discard_changes),
                            confirmationButtonText = getString(R.string.save),
                            titleText = getString(R.string.save_progress_expl)
                    )
            )
        }

        fragmentBooleanResult(DialogConfirmation.RESULT_KEY, DialogConfirmation.DEFAULT_REQ_KEY, onDenied = {
            findNavController().navigateUpSafe()
        }, onGranted = {
            prefsProvider.saveSizePref(prefBaseName + SIZE_PREF_ADDITION, binding.sizeSlider.value)
            pickedColor?.let { prefsProvider.saveColorPref(prefBaseName + COLOR_PREF_ADDITION, it) }
            pickedLayoutPosition?.let { prefsProvider.savePositionPref(prefBaseName + POSITION_PREF_ADDITION, it) }
            goBack()
        })
    }

    private fun goBack() {
        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenResumed {
            onMain {
                findNavController().navigateUpSafe()
                shortToast(R.string.saved)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pickedColor?.let { outState.putInt(COLOR_STATE, it) }
        pickedLayoutPosition?.let { outState.putInt(LAYOUT_STATE, it) }
        pickedSize?.let { outState.putFloat(SIZE_STATE, it) }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        pickedColor = savedInstanceState?.getInt(COLOR_STATE, defaultColor)
                ?: defaultColor
        pickedLayoutPosition = savedInstanceState?.getInt(LAYOUT_STATE, defaultLayoutPosition)
                ?: defaultLayoutPosition
        pickedSize = savedInstanceState?.getFloat(SIZE_STATE, defaultSize) ?: defaultSize

        updatePreviewColor(pickedColor!!)
        updatePreviewWidthAndHeight(pickedSize!!)
        binding.sizeSlider.value = pickedSize!!
        binding.layoutPosition.setSelection(pickedLayoutPosition!!)
    }

    private fun showDialogPicker(context: Context) {
        ColorPickerDialog.Builder(context).apply {
            setTitle(getString(R.string.pick_dot_color))
            setPreferenceName(prefBaseName)
            setPositiveButton(getString(R.string.select), ColorEnvelopeListener { envelope, _ -> setDotColor(envelope) })
            setNegativeButton(getString(R.string.cancel)
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
            setBottomSpace(12)
            show()
        }
    }

    private fun setDotColor(envelope: ColorEnvelope?) {
        envelope ?: return
        pickedColor = envelope.color
        updatePreviewColor(pickedColor!!)
    }

    private fun updatePreviewWidthAndHeight(value: Float) {
        binding.preview.width(value)
        binding.preview.height(value)
    }
}