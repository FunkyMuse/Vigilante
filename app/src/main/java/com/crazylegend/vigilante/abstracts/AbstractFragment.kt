package com.crazylegend.vigilante.abstracts

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.crazylegend.coroutines.withMainContext
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.kotlinextensions.fragments.finish
import com.crazylegend.kotlinextensions.fragments.viewCoroutineScope

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */
abstract class AbstractFragment<BINDING : ViewBinding>(contentLayoutId: Int) : Fragment(contentLayoutId) {

    abstract val binding: BINDING

    fun goToScreen(directions: NavDirections) {
        onResumedUIFunction { findNavController().navigate(directions) }
    }

    inline fun onResumedUIFunction(crossinline action: () -> Unit) {
        viewCoroutineScope.launchWhenResumed {
            withMainContext {
                action()
            }
        }
    }

    fun throwMissingArgException() {
        CrashyReporter.logException(IllegalStateException("Argument is missing in customization"))
        finish()
    }
}