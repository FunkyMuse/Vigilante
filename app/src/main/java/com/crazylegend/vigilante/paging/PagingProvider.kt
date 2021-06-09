package com.crazylegend.vigilante.paging

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

/**
 * Created by funkymuse on 6/9/21 to long live and prosper !
 */

@ViewModelScoped
class PagingProvider @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    PagingProviderContract {

    private companion object {
        private const val SCROLL_POS_TAG = "saveScrollPos"
    }

    override val getScrollPosition get() = savedStateHandle.get<Parcelable>(SCROLL_POS_TAG)

    override fun saveScrollPos(position: Parcelable?) {
        savedStateHandle.set(SCROLL_POS_TAG, position)
    }
}