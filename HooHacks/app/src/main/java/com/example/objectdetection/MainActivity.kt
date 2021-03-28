package com.example.objectdetection

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.example.objectdetection.databinding.ActivityMainBinding
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_SPEECH_INPUT = 100
    private lateinit var binding: ActivityMainBinding
    private lateinit var objectDetector: ObjectDetector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    lateinit var mTTS:TextToSpeech


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main) // data binding

        var speechButton = this.findViewById<ImageView>(R.id.imageView)

        speechButton.setOnClickListener(View.OnClickListener {

            getSpeechInput()
        })
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // get() is used to get the instance of the future.
            val cameraProvider = cameraProviderFuture.get()
            // Here, we will bind the preview
            bindPreview(cameraProvider = cameraProvider)
        }, ContextCompat.getMainExecutor(this))

        val localModel = LocalModel.Builder()
            .setAssetFilePath("sample1.tflite")
            .build()

        val customObjectDetectorOptions =
            CustomObjectDetectorOptions.Builder(localModel)
                .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
                .enableClassification()
                .setClassificationConfidenceThreshold(0.5f)
                .setMaxPerObjectLabelCount(3)
                .build()

        objectDetector = ObjectDetection.getClient(customObjectDetectorOptions)

        var gotoMAP = this.findViewById<Button>(R.id.gotoMAP)
        var gotoLearn = this.findViewById<Button>(R.id.gotoLearn)
        gotoMAP.setOnClickListener {
            val intent = Intent(this, Maps::class.java)
            startActivity(intent)
        }
        gotoLearn.setOnClickListener {
            val intent = Intent(this, Learn::class.java)
            startActivity(intent)
        }
    }

    private fun getSpeechInput()
    {
        val intent = Intent(RecognizerIntent
            .ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault())

        if (intent.resolveActivity(packageManager) != null)
        {
            startActivityForResult(intent, 10)
        } else
        {
            Toast.makeText(this,
                "Your Device Doesn't Support Speech Input",
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode,
            resultCode, data)
        var speeechTextt = this.findViewById<TextView>(R.id.editWordText)
        when (requestCode) {
            10 -> if (resultCode == RESULT_OK &&
                data != null)
            {
                val result =
                    data.
                    getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS)
                speeechTextt.text = result[0]
            }
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        var textView = this.findViewById<TextView>(R.id.TextObject)
        val preview : Preview = Preview.Builder().build()
        val cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), ImageAnalysis.Analyzer { imageProxy ->
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

                            if (binding.layout.childCount > 4) binding.layout.removeViewAt(1)

                            textView.text = it.labels.firstOrNull()?.text ?: ""

                            mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
                                if (status != TextToSpeech.ERROR) {
                                    mTTS.language = Locale.US
                                }
                            })

                            textView.addTextChangedListener(object : TextWatcher {
                                val textSpeech = textView.text.toString()
                                override fun afterTextChanged(s: Editable) {
                                    if (mTTS.isSpeaking) {
                                        mTTS.stop()
                                    }
                                }
                                override fun beforeTextChanged(s: CharSequence, start: Int,
                                                               count: Int, after: Int) {
                                    if (mTTS.isSpeaking) {
                                        mTTS.stop()
                                    }
                                }
                                override fun onTextChanged(s: CharSequence, start: Int,
                                                           before: Int, count: Int) {
                                        mTTS.speak(textSpeech, TextToSpeech.QUEUE_FLUSH, null)
                                }
                            })
                        }
                        imageProxy.close()
                    }
            }
        })


        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, imageAnalysis, preview)
    }
}
