package com.crazylegend.vigilante.abstracts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.crazylegend.vigilante.di.providers.AdapterProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
abstract class AbstractBottomSheet<Binding : ViewBinding> : BottomSheetDialogFragment() {

    @Inject
    lateinit var adapterProvider: AdapterProvider

    abstract val binding: Binding

    abstract val viewRes: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(viewRes, container, false)

}