package com.crazylegend.vigilante.abstracts

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.crazylegend.navigation.navigateSafe

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */
abstract class AbstractFragment<BINDING : ViewBinding>(contentLayoutId: Int) : Fragment(contentLayoutId) {

    abstract val binding: BINDING

    fun goToScreen(directions: NavDirections) {
        uiAction { findNavController().navigateSafe(directions) }
    }

    inline fun uiAction(crossinline action: () -> Unit) {
        if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            action()
        }
    }

    inline fun onStartedRepeatingAction(crossinline action: suspend () -> Unit) {
        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
            action()
        }
    }
}