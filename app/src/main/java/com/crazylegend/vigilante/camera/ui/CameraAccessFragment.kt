package com.crazylegend.vigilante.camera.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.crazylegend.database.handle
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
@AndroidEntryPoint
class CameraAccessFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler) {

    private val cameraAccessVM by viewModels<CameraAccessVM>()
    override val binding: LayoutRecyclerBinding by viewBinding(LayoutRecyclerBinding::bind)
    private val cameraAccessAdapter by lazy {
        adapterProvider.cameraAccessAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = cameraAccessAdapter

        cameraAccessVM.cameraAccess.observe(viewLifecycleOwner) {
            it.handle(
                    queryingDB = {

                    },
                    emptyDB = {

                    },
                    dbError = { throwable ->

                    },
                    success = {
                        cameraAccessAdapter.submitList(this)
                    }
            )
        }

    }
}