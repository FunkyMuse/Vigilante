package com.crazylegend.vigilante.power.details

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.database.DBResult
import com.crazylegend.database.handle
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.views.visibleIfTrueGoneOtherwise
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractBottomSheet
import com.crazylegend.vigilante.databinding.DialogPowerDetailsBinding
import com.crazylegend.vigilante.di.providers.prefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.power.db.PowerModel
import com.crazylegend.vigilante.utils.assistedViewModel
import com.crazylegend.vigilante.utils.onStartedRepeatingAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
@AndroidEntryPoint
class PowerDetailsDialog : AbstractBottomSheet<DialogPowerDetailsBinding>() {

    @Inject
    lateinit var prefsProvider: DefaultPreferencessProvider

    override val binding by viewBinding(DialogPowerDetailsBinding::bind)
    override val viewRes: Int
        get() = R.layout.dialog_power_details

    private val args by navArgs<PowerDetailsDialogArgs>()

    @Inject
    lateinit var powerDetailsVMFactory: PowerDetailsViewModel.PowerDetailsVMFactory

    private val powerDetailsVM by assistedViewModel {
        powerDetailsVMFactory.create(args.powerID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onStartedRepeatingAction {
            powerDetailsVM.powerModel.collectLatest {
                handleDBResult(it)
            }
        }
    }

    private fun handleDBResult(dbResult: DBResult<PowerModel>) {
        binding.loading.visibleIfTrueGoneOtherwise(dbResult is DBResult.Querying)
        dbResult.handle(
                dbError = { throwable ->
                    handleDBError(throwable)
                },
                success = {
                    updateUI(this)
                }
        )
    }

    private fun handleDBError(throwable: Throwable) {
        CrashyReporter.logException(throwable)
        dismissAllowingStateLoss()
        shortToast(R.string.error_occurred)
    }

    private fun updateUI(model: PowerModel) {
        binding.powerIcon.setImageResource(model.chargingDrawable)
        binding.powerTitle.text = getString(model.chargingTitle)
        binding.date.image.setImageResource(R.drawable.ic_calendar)
        binding.date.text.text = model.date.toString(prefsProvider.getDateFormat)
        binding.chargingType.root.visibleIfTrueGoneOtherwise(model.chargingTypeTitle != null)
        model.chargingTypeDrawable?.let { binding.chargingType.image.setImageResource(it) }
        binding.chargingType.text.text = model.chargingTypeTitle?.let { getString(it) }
        binding.batteryCapacity.text.text = model.batteryPercentage.toString()
        binding.batteryCapacity.image.setImageResource(R.drawable.ic_percentage)
    }
}