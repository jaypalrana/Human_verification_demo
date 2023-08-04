package com.humanverificationdemo.activities

import android.graphics.*
import android.os.*
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.mediapipe.components.CameraHelper
import com.google.mediapipe.components.CameraXPreviewHelper
import com.google.mediapipe.components.ExternalTextureConverter
import com.google.mediapipe.components.FrameProcessor
import com.google.mediapipe.components.PermissionHelper
import com.google.mediapipe.formats.proto.LandmarkProto
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList
import com.google.mediapipe.framework.AndroidAssetUtil
import com.google.mediapipe.framework.Packet
import com.google.mediapipe.framework.PacketGetter
import com.google.mediapipe.glutil.EglManager
import com.humanverificationdemo.R
import com.humanverificationdemo.databinding.ActivityMainBinding
import com.humanverificationdemo.utils.*
import com.humanverificationdemo.viewmodels.MainActivityViewModel
import java.io.File
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

class MainActivity : BaseActivity<ActivityMainBinding,MainActivityViewModel>() {
    private val TAG = "MainActivity"
    private val FOCAL_LENGTH_STREAM_NAME = "focal_length_pixel"

    private val OUTPUT_LANDMARKS_STREAM_NAME = "face_landmarks_with_iris"

    private var haveAddedSidePackets = false

    private var ry1 = 0f
    private var ry2: kotlin.Float = 0f
    private var ay1: kotlin.Float = 0f
    private var ay2: kotlin.Float = 0f
    private var ratio: kotlin.Float = 0f

    private var eye_blinked = false
    private var eye_open: Boolean = false
    var eyesBlinkCount = 0;

    private var leftEyeCanvas: CanvasView? = null
    private var rightEyeCanvas: CanvasView? = null
    private var chinCanvas: CanvasView? = null
    private var leftCheekCanvas: CanvasView? = null
    private var rightCheekCanvas: CanvasView? = null
    private var foreHeadCanvas: CanvasView? = null

    private lateinit var outputDirectory: File
    var processor: FrameProcessor? = null

    // Handles camera access via the {@link CameraX} Jetpack support library.
    var cameraHelper: CameraXPreviewHelper? = null

    // {@link SurfaceTexture} where the camera-preview frames can be accessed.
    var previewFrameTexture: SurfaceTexture? = null

    // {@link SurfaceView} that displays the camera-preview frames processed by a MediaPipe graph.
    var previewDisplayView: SurfaceView? = null

    // Creates and manages an {@link EGLContext}.
    var eglManager: EglManager? = null

    // Converts the GL_TEXTURE_EXTERNAL_OES texture from Android camera into a regular texture to be
    // consumed by {@link FrameProcessor} and the underlying MediaPipe graph.
    var converter: ExternalTextureConverter? = null


    var eyesBlinkDone = false
    var isFaceDetected = false
    var isFaceAligned = false
    var isLeftSideMovementDone = false
    var isRightSideMovementDone = false
    var isMovementOfHeadDone = false
    var isCenterSideMovementDone = false

    var surfaceHolder: SurfaceHolder? = null

    var height: Int = 0
    var width: Int = 0

    override fun getLayoutId() = R.layout.activity_main

    override fun getViewModel(): MainActivityViewModel {
        return ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainActivityViewModel::class.java)
    }

    override fun setOnClick() {

    }

    override fun apiObserve() {

    }

    override fun initialize() {
            AndroidAssetUtil.initializeNativeAssetManager(this)
            eglManager = EglManager(null)
            try {
                processor = FrameProcessor(
                    this,
                    eglManager?.getNativeContext()!!,
                    "iris_tracking_gpu.binarypb",
                    "input_video",
                    "input_video"
                )
            } catch (e: Exception) {
                Logger.v("-->", "${e.message}")
            }

            processor!!
                .videoSurfaceOutput
                .setFlipY(true)

            PermissionHelper.checkAndRequestCameraPermissions(this)
            setProgressData()

            eye_open = true
            eye_blinked = true

            outputDirectory = getOutputDirectory()

        getViewBinding().tvFaceDetection.show()

            processor!!.addPacketCallback(
                OUTPUT_LANDMARKS_STREAM_NAME
            ) { packet ->
                packetReceived = true

                try {

                    var landmarksRaw = PacketGetter.getProtoBytes(packet)
                    val landmarks = NormalizedLandmarkList.parseFrom(landmarksRaw)


                    var leftIrisLandmarks = ArrayList<LandmarkProto.NormalizedLandmark>()
                    leftIrisLandmarks.add(landmarks.getLandmark(468)) // center
                    leftIrisLandmarks.add(landmarks.getLandmark(470)) // top
                    leftIrisLandmarks.add(landmarks.getLandmark(472)) // bottom
                    leftIrisLandmarks.add(landmarks.getLandmark(471)) // left
                    leftIrisLandmarks.add(landmarks.getLandmark(469)) // right

                    var rightIrisLandMarks = ArrayList<LandmarkProto.NormalizedLandmark>()
                    rightIrisLandMarks.add(landmarks.getLandmark(473)) // center
                    rightIrisLandMarks.add(landmarks.getLandmark(475)) // top
                    rightIrisLandMarks.add(landmarks.getLandmark(477)) // bottom
                    rightIrisLandMarks.add(landmarks.getLandmark(474)) // left
                    rightIrisLandMarks.add(landmarks.getLandmark(476)) // right


                    var left_iris_size = calculateIrisDiameter(leftIrisLandmarks)
                    var right_iris_size = calculateIrisDiameter(rightIrisLandMarks)


                    var leftEyeDepth: Float = getDepthOfEye(landmarks.getLandmark(468), left_iris_size)

                    var rightEyeDepth: Float =
                        getDepthOfEye(landmarks.getLandmark(473), right_iris_size)


                    var leftCheek = landmarks.getLandmark(187)
                    var rightCheek = landmarks.getLandmark(411)
                    var forehead = landmarks.getLandmark(10)
                    var chin = landmarks.getLandmark(152)


                    var foreheadLandmarks = ArrayList<LandmarkProto.NormalizedLandmark>()
                    foreheadLandmarks.add(landmarks.getLandmark(9)) //center
                    foreheadLandmarks.add(landmarks.getLandmark(66)) // left
                    foreheadLandmarks.add(landmarks.getLandmark(296)) // right
                    foreheadLandmarks.add(landmarks.getLandmark(151)) //top
                    foreheadLandmarks.add(landmarks.getLandmark(8)) //bottom


                    var chinLandmarks = ArrayList<LandmarkProto.NormalizedLandmark>()
                    chinLandmarks.add(landmarks.getLandmark(199)) //center
                    chinLandmarks.add(landmarks.getLandmark(140)) //left
                    chinLandmarks.add(landmarks.getLandmark(369)) //right
                    chinLandmarks.add(landmarks.getLandmark(200)) //top
                    chinLandmarks.add(landmarks.getLandmark(152)) //bottom

                    var min_value_forehead = min(leftEyeDepth, rightEyeDepth)
                    var abs_value_forehead = abs(leftEyeDepth - rightEyeDepth) / 2

                    var forehead_depth = min_value_forehead + abs_value_forehead

                    var chin_depth =
                        min_value_forehead + abs_value_forehead

                    var left_cheek_depth = leftEyeDepth
                    var right_cheek_depth = rightEyeDepth


                    runOnUiThread {
                        var leftEyeDepthInCm: Int = leftEyeDepth.toInt() / 10
                        var rightEyeDepthInCm: Int = rightEyeDepth.toInt() / 10


                        handler.removeCallbacks(runnable)
                        if (!isFaceDetected && leftEyeDepthInCm in 40..60 && rightEyeDepthInCm in 40..60) {
                            isFaceDetected = true
                            showMessages(1) // face detected
                        } else if (isFaceDetected && leftEyeDepthInCm in 40..60 && rightEyeDepthInCm in 40..60) {
                            if (!isLeftSideMovementDone) {
                                showMessages(3) // turn head slowly left
                            } else if (isLeftSideMovementDone && !isRightSideMovementDone) {
                                showMessages(5) // turn head slowly right
                            } else if (isLeftSideMovementDone && isRightSideMovementDone && isFaceDetected && !eyesBlinkDone) {
                                showMessages(6) //blink your eyes 2 times
                                isMovementOfHeadDone = true
                            }
                            if (isMovementOfHeadDone && !eyesBlinkDone) {
                                ry1 = landmarks.landmarkList[5].y * 1920f
                                ry2 = landmarks.landmarkList[4].y * 1920f

                                ay1 = landmarks.landmarkList[373].y * 1920f
                                ay2 = landmarks.landmarkList[386].y * 1920f

                                ratio = (ay1 - ay2) / (ry2 - ry1)

                                if (ratio < 0.8f) {
                                    if (eye_blinked) {
                                        Logger.v("Test-->", "Eye is blinked")
                                        eye_blinked = false
                                        eye_open = true
                                        eyesBlinkCount++
                                        if (eyesBlinkCount == 2) {
                                            eyesBlinkCount = 0
                                            eyesBlinkDone = true
                                            var strJson = Gson().toJson(landmarks)


                                            Logger.v("ForeHead X:${forehead.x} Y:${forehead.y}")
                                            Logger.v("Chin X:${chin.x} Y:${chin.y}")
                                            Logger.v("Left cheek X:${leftCheek.x} Y:${leftCheek.y}")
                                            Logger.v("Right cheek X:${rightCheek.x} Y:${rightCheek.y}")

                                            SharedPrefsUtils.landmarks = strJson
                                            SharedPrefsUtils.width = surfaceWidth
                                            SharedPrefsUtils.height = surfaceHeight
                                            SharedPrefsUtils.leftCheekX = leftCheek.x
                                            SharedPrefsUtils.leftCheekY = leftCheek.y
                                            SharedPrefsUtils.rightCheekX = rightCheek.x
                                            SharedPrefsUtils.rightCheekY = rightCheek.y
                                            SharedPrefsUtils.foreheadX = forehead.x
                                            SharedPrefsUtils.foreheadY = forehead.y
                                            SharedPrefsUtils.chinX = chin.x
                                            SharedPrefsUtils.chinY = chin.y
                                            showMessages(7) //eyes blinked 2 times
                                        }
                                    }
                                } else {
                                    if (eye_open) {
//                                    Logger.v("Test-->", "Eye is open")
                                        eye_blinked = true
                                        eye_open = false
                                    }
                                }
                            }
                        } else if (isFaceDetected && !isLeftSideMovementDone && leftEyeDepthInCm > 80 && rightEyeDepthInCm in 40..60) {
                            isLeftSideMovementDone = true
                            showMessages(4) // recenter head
                        } else if (isFaceDetected && isLeftSideMovementDone
                            && !isRightSideMovementDone && leftEyeDepthInCm in 40..60 && rightEyeDepthInCm > 80
                        ) {
                            isRightSideMovementDone = true
                            showMessages(4) // recenter head
                        } else if (isFaceDetected && leftEyeDepthInCm > 60 && rightEyeDepthInCm > 60 && !eyesBlinkDone) {
                            showMessages(8) //too far
                            isFaceDetected = false
                        } else if (isFaceDetected && leftEyeDepthInCm < 40 && rightEyeDepthInCm < 40 && !eyesBlinkDone) {
                            showMessages(9) // too close
                            isFaceDetected = false
                        }

                        if (leftEyeCanvas == null) {
                            leftEyeCanvas = CanvasView(
                                this,
                                leftIrisLandmarks.get(0).x * surfaceWidth,
                                leftIrisLandmarks.get(0).y * surfaceHeight,
                                "Left Eye: ${leftEyeDepth.toInt() / 10} cm"
                            )
                            getViewBinding().previewDisplayLayout.addView(
                                leftEyeCanvas
                            )
                        } else
                            leftEyeCanvas?.updateXAndY(
                                leftIrisLandmarks.get(0).x * surfaceWidth,
                                leftIrisLandmarks.get(0).y * surfaceHeight,
                                "Left Eye: ${leftEyeDepth.toInt() / 10} cm"
                            )

                        if (rightEyeCanvas == null) {
                            rightEyeCanvas = CanvasView(
                                this,
                                rightIrisLandMarks.get(0).x * surfaceWidth,
                                rightIrisLandMarks.get(0).y * surfaceHeight,
                                "Right Eye: ${rightEyeDepth.toInt() / 10} cm"
                            )
                            getViewBinding().previewDisplayLayout.addView(
                                rightEyeCanvas
                            )
                        } else
                            rightEyeCanvas?.updateXAndY(
                                rightIrisLandMarks.get(0).x * surfaceWidth,
                                rightIrisLandMarks.get(0).y * surfaceHeight,
                                "Right Eye: ${rightEyeDepth.toInt() / 10} cm"
                            )

                        if (chinCanvas == null) {
                            chinCanvas = CanvasView(
                                this,
                                chin.x * surfaceWidth,
                                chin.y * surfaceHeight,
                                "Chin: ${chin_depth.toInt() / 10 - 1} cm"
                            )
                            getViewBinding().previewDisplayLayout.addView(
                                chinCanvas
                            )

                        } else {
                            chinCanvas?.updateXAndY(
                                chin.x * surfaceWidth, chin.y * surfaceHeight,
                                "Chin: ${chin_depth.toInt() / 10 - 1} cm"
                            )
                        }

                        if (leftCheekCanvas == null) {
                            leftCheekCanvas = CanvasView(
                                this,
                                leftCheek.x * surfaceWidth,
                                leftCheek.y * surfaceHeight,
                                "Left Cheek: ${left_cheek_depth.toInt() / 10 + 1} cm"
                            )
                            getViewBinding().previewDisplayLayout.addView(
                                leftCheekCanvas
                            )

                        } else {
                            leftCheekCanvas?.updateXAndY(
                                leftCheek.x * surfaceWidth,
                                leftCheek.y * surfaceHeight,
                                "Left Cheek: ${left_cheek_depth.toInt() / 10 + 1} cm"
                            )
                        }


                        if (rightCheekCanvas == null) {
                            rightCheekCanvas = CanvasView(
                                this,
                                rightCheek.x * surfaceWidth,
                                rightCheek.y * surfaceHeight,
                                "Right Cheek: ${left_cheek_depth.toInt() / 10 + 1} cm"
                            )
                            getViewBinding().previewDisplayLayout.addView(
                                rightCheekCanvas
                            )

                        } else {
                            rightCheekCanvas?.updateXAndY(
                                rightCheek.x * surfaceWidth, rightCheek.y * surfaceHeight,
                                "Right Cheek: ${left_cheek_depth.toInt() / 10 + 1} cm"
                            )
                        }

                        if (foreHeadCanvas == null) {
                            foreHeadCanvas = CanvasView(
                                this,
                                forehead.x * surfaceWidth,
                                forehead.y * surfaceHeight,
                                "Forehead: ${forehead_depth.toInt() / 10 - 1} cm"
                            )
                            getViewBinding().previewDisplayLayout.addView(
                                foreHeadCanvas
                            )

                        } else {
                            foreHeadCanvas?.updateXAndY(
                                forehead.x * surfaceWidth, forehead.y * surfaceHeight,
                                "Forehead: ${forehead_depth.toInt() / 10 - 1} cm"
                            )
                        }

                        packetReceived = false

                        handler.postDelayed(runnable, 250)
                    }

                } catch (e: Exception) {

                }
            }

    }


     fun onCameraStarted(surfaceTexture: SurfaceTexture) {

        // onCameraStarted gets called each time the activity resumes, but we only want to do this once.

        // onCameraStarted gets called each time the activity resumes, but we only want to do this once.
         previewFrameTexture = surfaceTexture
         // Make the display view visible to start showing the preview. This triggers the
         // SurfaceHolder.Callback added to (the holder of) previewDisplayView.
         previewDisplayView!!.show()
        if (!haveAddedSidePackets) {
            val focalLength = cameraHelper!!.focalLengthPixels
            if (focalLength != Float.MIN_VALUE) {
                val focalLengthSidePacket = processor!!.packetCreator.createFloat32(focalLength)
                val inputSidePackets: MutableMap<String, Packet> = HashMap()
                inputSidePackets[FOCAL_LENGTH_STREAM_NAME] = focalLengthSidePacket
                processor!!.setInputSidePackets(inputSidePackets)
            }
            haveAddedSidePackets = true
        }
    }

    var packetReceived = false

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setProgressData() {
        getViewBinding().progressBar.apply {
            progress = 0

        }
    }

    var handler = Handler(Looper.getMainLooper())
    private var runnable = Runnable {
        runOnUiThread {
            if (!packetReceived) {
                if (isFaceDetected) {
                    isFaceDetected = false
                    getViewBinding().tvFaceDetection.show()
                    getViewBinding().tvFaceDetectionTop.text = "Face Not Detected"
                    getViewBinding().tvFaceDetectionMsg.text = "Please position your face in the frame"
                    getViewBinding().tvFaceDetection.text = "Please align your face in center of frame"
                    clearAllCanvas()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        converter!!.close()

        // Hide preview display until we re-open the camera again.

        // Hide preview display until we re-open the camera again.
        previewDisplayView!!.hide()
        setProgressData()
        clearAllCanvas()
    }

    private fun clearAllCanvas() {
        leftEyeCanvas?.clear()
        rightEyeCanvas?.clear()
        foreHeadCanvas?.clear()
        leftCheekCanvas?.clear()
        rightCheekCanvas?.clear()
        chinCanvas?.clear()
    }

    private fun getDepthOfEye(
        landmark: LandmarkProto.NormalizedLandmark,
        eye_iris_size: Float
    ): Float {


        var y = getDepth(
            width / 2f, height / 2f, landmark.x * width, landmark.y * height
        )

        var x = sqrt(cameraHelper!!.focalLengthPixels * cameraHelper!!.focalLengthPixels + y * y)
        var depth: Float = 0f
        depth = 11.8f * x / eye_iris_size
        return depth
    }

    fun calculateIrisDiameter(normalizedLandmarkList: ArrayList<LandmarkProto.NormalizedLandmark>): Float {
        var dist_vert: Float = getLandmarkDepth(
            normalizedLandmarkList[1],
            normalizedLandmarkList[2]
        )
        var dist_hori: Float = getLandmarkDepth(
            normalizedLandmarkList[3],
            normalizedLandmarkList[4]
        )

        return (dist_hori + dist_vert) / 2.0f
    }

    fun getLandmarkDepth(
        ld0: LandmarkProto.NormalizedLandmark,
        ld1: LandmarkProto.NormalizedLandmark
    ): Float {
        return getDepth(ld0.x * width, ld0.y * height, ld1.x * width, ld1.y * height)
    }

    private fun getDepth(x0: Float, y0: Float, x1: Float, y1: Float): Float {
        return sqrt(((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1)).toDouble()).toFloat()
    }


    private fun showMessages(code: Int) {
        getViewBinding().tvFaceDetection.show()
        Logger.v("App", "face detected : $code")
        when (code) {
            1 -> {
                getViewBinding().tvFaceDetectionTop.text = "Perfect!"
                getViewBinding().tvFaceDetectionMsg.text = ""
                getViewBinding().tvFaceDetection.text = "Face Detected"
                getViewBinding().cardScanning.hide()
                getViewBinding().progressBar.show()
            }
            2 -> {
                if (getViewBinding().tvFaceDetection.text.toString() != "Please align your face in center of frame") {
                    getViewBinding().tvFaceDetectionTop.text = "Face Not In Center"
                    getViewBinding().tvFaceDetectionMsg.text =
                        "Please set your complete face inside the frame."
                    getViewBinding().tvFaceDetection.text = "Please align your face in center of frame"
                }
            }
            3 -> {
                getViewBinding().tvFaceDetectionTop.text = "Perfect!"
                getViewBinding().tvFaceDetectionMsg.text = ""
                getViewBinding().tvFaceDetection.text = "Now turn your head slowly to the left"
                getViewBinding().progressBar.progress = 17
            }
            4 -> {
                getViewBinding().tvFaceDetectionTop.text = "Perfect!"
                getViewBinding().tvFaceDetectionMsg.text = ""
                getViewBinding().tvFaceDetection.text = "Now turn your head slowly to the center"
                if (isLeftSideMovementDone && !isRightSideMovementDone)
                    getViewBinding().progressBar.progress = 34
                else if (isRightSideMovementDone)
                    getViewBinding().progressBar.progress = 68

            }
            5 -> {
                getViewBinding().tvFaceDetectionTop.text = "Perfect!"
                getViewBinding().tvFaceDetectionMsg.text = ""
                getViewBinding().tvFaceDetection.text = "Now turn your head slowly to the right"
                if (isLeftSideMovementDone && !isRightSideMovementDone)
                    getViewBinding().progressBar.progress = 51

            }
            6 -> {
                getViewBinding().tvFaceDetectionTop.text = "Perfect!"
                getViewBinding().tvFaceDetectionMsg.text = ""
                getViewBinding().tvFaceDetection.text =
                    "Good Job!\nOpen Your Eyes Fully And Blink Them Two Times"
                getViewBinding().progressBar.progress = 82
            }
            8 -> {
                getViewBinding().tvFaceDetectionTop.text = "Too Far"
                getViewBinding().tvFaceDetectionMsg.text = "Move your phone close to your face"
                getViewBinding().tvFaceDetection.text = "Please position your face in the square."
            }
            9 -> {
                getViewBinding().tvFaceDetectionTop.text = "Too Close"
                getViewBinding().tvFaceDetectionMsg.text = "Move your phone close to your face"
                getViewBinding().tvFaceDetection.text = "Please position your face in the square."
            }
            10 -> {
                getViewBinding().tvFaceDetectionTop.text = "Face Not In Center"
                getViewBinding().tvFaceDetectionMsg.text =
                    "Please set your complete face inside the frame"
                getViewBinding().tvFaceDetection.text = "Please position your face in the square."
            }
            11 -> {
                getViewBinding().tvFaceDetectionTop.text = "Aligned!"
                getViewBinding().tvFaceDetectionMsg.text = ""
                getViewBinding().tvFaceDetection.text = ""

            }
            7 -> {


                getViewBinding().progressBar.progress = 100
                Toast.makeText(applicationContext,"Face Detected Successfully",Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        isCenterSideMovementDone = false
        isFaceDetected = false
        isLeftSideMovementDone = false;
        isMovementOfHeadDone = false;
        isRightSideMovementDone = false
    }

    override fun onResume() {
        super.onResume()
        converter = ExternalTextureConverter(
            eglManager!!.context,
            2
        )
        converter!!.setFlipY(true)

        converter!!.setConsumer(processor)
        if (PermissionHelper.cameraPermissionsGranted(this)) {
            startCamera()
        }

        previewDisplayView = SurfaceView(this)
        setupPreviewDisplayView()

    }

    protected open fun cameraTargetResolution(): Size? {
        return null // No preference and let the camera (helper) decide.
    }

    open fun startCamera() {
        cameraHelper = CameraXPreviewHelper()
        previewFrameTexture = converter!!.surfaceTexture
        cameraHelper!!.setOnCameraStartedListener { surfaceTexture: SurfaceTexture? ->
            onCameraStarted(
                surfaceTexture!!
            )
        }
        val cameraFacing = CameraHelper.CameraFacing.FRONT

        cameraHelper!!.startCamera(
            this, cameraFacing, previewFrameTexture, cameraTargetResolution()
        )

    }

    protected open fun computeViewSize(width: Int, height: Int): Size {
        return Size(width, height)
    }

    var surfaceWidth: Int = 0
    var surfaceHeight: Int = 0

    protected open fun onPreviewDisplaySurfaceChanged(
        holder: SurfaceHolder?, format: Int, width: Int, height: Int
    ) {
        // (Re-)Compute the ideal size of the camera-preview display (the area that the
        // camera-preview frames get rendered onto, potentially with scaling and rotation)
        // based on the size of the SurfaceView that contains the display.
        val viewSize = computeViewSize(width, height)
        val displaySize = cameraHelper!!.computeDisplaySizeFromViewSize(viewSize)
        val isCameraRotated = cameraHelper!!.isCameraRotated
        // Configure the output width and height as the computed display size.
        converter!!.setDestinationSize(
            if (isCameraRotated) displaySize.height else displaySize.width,
            if (isCameraRotated) displaySize.width else displaySize.height
        )


        Logger.v("Test-->", "size:$displaySize")
        Logger.v("Test-->", "size:$viewSize")

        this.surfaceWidth = width
        this.surfaceHeight = height

        Logger.v("surfaceWidth:$surfaceWidth  surfaceHeight:$surfaceHeight")

        this.width = displaySize?.width!!
        this.height = displaySize?.height!!
    }

    fun setupPreviewDisplayView() {
        previewDisplayView!!.hide()
        val viewGroup = findViewById<ViewGroup>(R.id.preview_display_layout)
        viewGroup.addView(previewDisplayView)
        var textureView = TextureView(this)

        previewDisplayView!!
            .getHolder()
            .addCallback(
                object : SurfaceHolder.Callback {
                    override fun surfaceCreated(holder: SurfaceHolder) {
                        Logger.v("Test-->", "Surface Created")
                        processor!!.videoSurfaceOutput.setSurface(holder.surface)
                    }

                    override fun surfaceChanged(
                        holder: SurfaceHolder,
                        format: Int,
                        width: Int,
                        height: Int
                    ) {
                        Logger.v("Test-->", "SurfaceChanged")
                        surfaceHolder = holder
                        onPreviewDisplaySurfaceChanged(holder, format, width, height)
                    }

                    override fun surfaceDestroyed(holder: SurfaceHolder) {
                        Logger.v("Test-->", "SurfaceDestroyed")
                        processor!!.videoSurfaceOutput.setSurface(null)

                    }
                })
    }

}