package com.example.hoohacks

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.example.hoohacks.databinding.FragmentTranslationBinding
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions

class TranslationFragment : Fragment() {
        private lateinit var binding: FragmentTranslationBinding
        private lateinit var objectDetector: ObjectDetector
        private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_translation, container, false)

            cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

            cameraProviderFuture.addListener(Runnable {
                // get() is used to get the instance of the future.
                val cameraProvider = cameraProviderFuture.get()
                // Here, we will bind the preview
                bindPreview(cameraProvider = cameraProvider)
            }, ContextCompat.getMainExecutor(context))

            val localModel = LocalModel.Builder()
                .setAssetFilePath("demo.tflite")
                .build()

            val customObjectDetectorOptions =
                CustomObjectDetectorOptions.Builder(localModel)
                    .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
                    .enableClassification()
                    .setClassificationConfidenceThreshold(0.5f)
                    .setMaxPerObjectLabelCount(3)
                    .build()

            objectDetector = ObjectDetection.getClient(customObjectDetectorOptions)
        return inflater.inflate(R.layout.fragment_translation, container, false)
    }


    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        val preview : Preview = Preview.Builder().build()

        val cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280,720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireActivity()), ImageAnalysis.Analyzer { imageProxy ->
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val image = imageProxy.image
            if(image != null) {

                val inputImage = InputImage.fromMediaImage(image, rotationDegrees)
                objectDetector
                    .process(inputImage)
                    .addOnFailureListener {
                        imageProxy.close()
                    }.addOnSuccessListener { objects ->
                        for (it in objects) {
                            if (binding.layout.childCount > 1) binding.layout.removeViewAt(1)
                            val element = Draw(activity!!,
                                rect = it.boundingBox,
                                text = it.labels.firstOrNull()?.text ?: "Undefined")

                            binding.layout.addView(element, 1)

                        }

                        imageProxy.close()
                    }
            }
        })


        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, imageAnalysis, preview)

    }

    @SuppressLint("ViewConstructor")
    class Draw(context:  Context?, var rect: Rect, var text: String) : View(context) {

        private lateinit var paint: Paint
        private lateinit var textPaint: Paint

        init {
            init()
        }

        private fun init() {
            paint = Paint()
            paint.color = Color.RED
            paint.strokeWidth = 20f
            paint.style = Paint.Style.STROKE

            textPaint = Paint()
            textPaint.color = Color.RED
            textPaint.style = Paint.Style.FILL
            textPaint.textSize = 80f
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawText(text, rect.centerX().toFloat(), rect.centerY().toFloat(), textPaint)
            canvas.drawRect(rect.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.bottom.toFloat(), paint)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Initialize UI
    }

}