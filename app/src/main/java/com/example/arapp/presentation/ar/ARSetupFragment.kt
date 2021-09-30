package com.example.arapp.presentation.ar

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.arapp.R
import com.example.arapp.presentation.ar.compose.ARSetupBody
import com.example.arapp.util.ARUtil
import com.example.arapp.util.PermissionUtil
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ARSetupFragment : Fragment() {

    private val permissionUtil by inject<PermissionUtil>()
    private val arUtil by inject<ARUtil>()

    private val viewModel: ARSetupViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val isARSupported by viewModel.isARSupported.collectAsState()
                ARSetupBody(
                    isARSupported = isARSupported,
                    onARClick = { startARSetup() }
                )
            }
        }
    }

    private fun startARSetup() {
        viewLifecycleOwner.lifecycleScope.launch {
            permissionUtil.checkAndRequestPermission(requireActivity(), Manifest.permission.CAMERA)
                .collect {
                    if (it.first()) {
                        if (arUtil.isARServicesInstalled(
                                activity = requireActivity(),
                                onErrorAction = {
                                    Toast.makeText(
                                        requireContext(), "Something went wrong! Please try again later!", Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        ) {
                            findNavController().navigate(R.id.action_ar_setup_fragment_to_ar_fragment)
                        }
                    } else {
                        openAppSettings()
                    }
                }
        }
    }

    private fun openAppSettings() {
        startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
            }
        )
    }
}