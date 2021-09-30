package com.example.arapp.presentation.ar

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MotionEvent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.arapp.R
import com.example.arapp.databinding.FragmentArBinding
import com.example.arapp.presentation.ar.compose.CloseButton
import com.example.arapp.presentation.ar.compose.HorizontalModelPager
import com.example.arapp.presentation.ar.compose.HorizontalTexturePager
import com.example.arapp.presentation.ar.model.ModelTexture
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.ar.core.Config
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.lang.ref.WeakReference

@ExperimentalPagerApi
internal class ARFragment : Fragment(), BaseArFragment.OnTapArPlaneListener,
    ArFragment.OnViewCreatedListener {

    private lateinit var binding: FragmentArBinding
    private lateinit var arFragment: ArFragment
    private var model: Renderable? = null
    private lateinit var modelNode: TransformableNode
    private var texture: Texture? = null
    private val viewModel: ARViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComposableViews()
        childFragmentManager.addFragmentOnAttachListener { _: FragmentManager?, fragment: Fragment ->
            if (fragment.id == R.id.arFragment) {
                arFragment = fragment as ArFragment
                arFragment.setOnTapArPlaneListener(this@ARFragment)
                arFragment.setOnViewCreatedListener(this@ARFragment)
            }
        }

        childFragmentManager.beginTransaction()
            .add(R.id.arFragment, ArFragment::class.java, null)
            .commit()

        collectARModels()
    }

    override fun onViewCreated(arFragment: ArFragment?, arSceneView: ArSceneView?) {
        arSceneView?.planeRenderer?.isShadowReceiver = false

        val session = Session(requireContext())
        val config = Config(session).apply {
            updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
            lightEstimationMode = Config.LightEstimationMode.DISABLED
            augmentedFaceMode = Config.AugmentedFaceMode.DISABLED
            focusMode = Config.FocusMode.FIXED
            instantPlacementMode = Config.InstantPlacementMode.DISABLED
            cloudAnchorMode = Config.CloudAnchorMode.DISABLED
            planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
            depthMode = Config.DepthMode.AUTOMATIC
        }
        session.configure(config)
        arSceneView?.session = session
    }

    override fun onTapPlane(hitResult: HitResult?, plane: Plane?, motionEvent: MotionEvent?) {
        if (model == null) {
            Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
            return
        }
        if (hitResult != null) {
            val anchorNode = createAnchor(hitResult)
            modelNode = TransformableNode(arFragment.transformationSystem)
            modelNode.setParent(anchorNode)

            modelNode.setRenderable(model).apply {
                isShadowCaster = false
                isShadowReceiver = false
                if (texture != null) {
                    material.setInt("baseColorIndex", 0)
                    material.setTexture("baseColorMap", texture)
                }
                animate(true).start()
            }

            modelNode.scaleController.isEnabled = true
            modelNode.select()
        }
    }

    private fun loadModel(modelPath: String) {
        val weakActivity: WeakReference<ARFragment> = WeakReference(this)
        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse(modelPath))
            .setIsFilamentGltf(true)
            .build()
            .thenAccept { model: ModelRenderable ->
                val fragment: ARFragment? = weakActivity.get()
                if (fragment != null) {
                    fragment.model = model
                    model.isShadowCaster = false
                    model.isShadowReceiver = false
                }
            }
            .exceptionally { throwable: Throwable? ->
                Toast.makeText(requireContext(), "Unable to load model", Toast.LENGTH_LONG).show()
                Timber.e(throwable)
                null
            }
    }

    private fun loadTexture(path: String) {
        val weakActivity: WeakReference<ARFragment> = WeakReference(this)
        Texture.builder()
            .setSampler(
                Texture.Sampler.builder()
                    .setMinFilter(Texture.Sampler.MinFilter.LINEAR_MIPMAP_LINEAR)
                    .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                    .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
                    .build()
            )
            .setSource(requireContext(), Uri.parse(path))
            .setUsage(Texture.Usage.COLOR)
            .build()
            .thenAccept { texture ->
                val fragment: ARFragment? = weakActivity.get()
                fragment?.texture = texture
            }
            .exceptionally { throwable ->
                Toast.makeText(requireContext(), "Unable to load texture", Toast.LENGTH_LONG).show()
                Timber.e(throwable)
                null
            }
    }

    private fun createAnchor(hitResult: HitResult): AnchorNode {
        val anchor = hitResult.createAnchor()
        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(arFragment.arSceneView.scene)
        return anchorNode
    }

    private fun collectARModels() {
        lifecycleScope.launchWhenStarted {
            viewModel.selectedModel.collectLatest {
                loadModel(it.path)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.texture.collectLatest {
                if (it != ModelTexture.None) {
                    loadTexture(it.path)
                } else {
                    texture = null
                }
            }
        }
    }

    private fun initComposableViews() {
        binding.closeButton.setContent {
            CloseButton {
                findNavController().navigateUp()
            }
        }
        binding.modelHorizontalPager.setContent {
            HorizontalModelPager(
                models = viewModel.models,
                selectedItemIndex = { viewModel.selectModel(it) }
            )
        }
        binding.textureHorizontalPager.setContent {
            HorizontalTexturePager(
                models = viewModel.textures,
                selectedItemIndex = { viewModel.selectTexture(it) }
            )
        }
    }
}