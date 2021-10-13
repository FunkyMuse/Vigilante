package com.crazylegend.vigilante.crashes

import android.content.Context
import com.crazylegend.recyclerview.AbstractViewBindingHolderAdapter
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.databinding.ItemviewCrashBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by Hristijan, date 10/13/21
 */
@FragmentScoped
class CrashesAdapter @Inject constructor() : AbstractViewBindingHolderAdapter<String, ItemviewCrashBinding>(ItemviewCrashBinding::inflate) {

    override fun bindItems(item: String, position: Int, itemCount: Int, binding: ItemviewCrashBinding, context: Context) {
        binding.text.text = context.getString(R.string.crash_report, position + 1)
    }
}