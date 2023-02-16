package zoo.animals.feature_camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import org.tensorflow.lite.task.vision.detector.ObjectDetector.ObjectDetectorOptions
import zoo.animals.feature_category.data.Animal
import zoo.animals.getAnimalByName
import zoo.animals.ml.*


class ImageClassifier(private val context: Context) {

    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()

    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    @SuppressLint("UnsafeOptInUsageError")
    fun detect(imageProxy: ImageProxy): List<Any> {
        var text = ""
        val score: Float
        var info = ""

        var location = RectF()

        val options = ObjectDetectorOptions.builder()
            .setBaseOptions(BaseOptions.builder().build())
            .setMaxResults(1)
            .build()
        val objectDetector = ObjectDetector.createFromFileAndOptions(
//            context, "lite-model_efficientdet_lite3_detection_metadata_1.tflite", options
//            context, "ssddetect_metadata_animals.tflite", options
//            context, "ssddetect_OPS_metadata_ctvrte.tflite", options
//            context, "ssddetect_OPS_480_background_metadata10.tflite", options
//            context, "ssd_640_qua_metadata_13.tflite", options
            context, "ssd_640_background_removed_metadata_18.tflite", options
        )

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val resizedBitmap = CameraUtils.toBitmap(mediaImage)?.let {
                Bitmap.createScaledBitmap(
                    it,
                    640,
                    640,
                    false
                )
            }

            val matrix = Matrix()
            matrix.postRotate(90f)
            val rotatedBitmap = resizedBitmap?.let {
                Bitmap.createBitmap(
                    resizedBitmap,
                    0,
                    0,
                    resizedBitmap.width,
                    resizedBitmap.height,
                    matrix,
                    true
                )
            }

            val tfImage = TensorImage.fromBitmap(rotatedBitmap)
            val results: List<Detection> = objectDetector.detect(tfImage)

            if (results.isNotEmpty()){
                if (results[0].categories[0].score > 0.5f) {
                    text = results[0].categories[0].label
                    score = results[0].categories[0].score
                    info = text + " " + score
                    location = results[0].boundingBox
                }
            }
        }

        imageProxy.close()
        return listOf(text, location, info)
    }


    @SuppressLint("UnsafeOptInUsageError")
    fun classify(imageProxy: ImageProxy): List<Any?> {
//        val animalLabels: MutableList<String> = FileUtil.loadLabels(context, "labels.txt")

        var result = ""
        val image = imageProxy.image ?: return listOf("", "")

        val resizedBitmap = CameraUtils.toBitmap(image)?.let {
            Bitmap.createScaledBitmap(
                it,
                280,
                280,
                false
            )
        }

        val matrix = Matrix()
        matrix.postRotate(90f)
        val rotatedBitmap = resizedBitmap?.let {
            Bitmap.createBitmap(
                resizedBitmap,
                0,
                0,
                resizedBitmap.width,
                resizedBitmap.height,
                matrix,
                true
            )
        }


//        val model = ModelNewAnimals.newInstance(context)
        val model = AutoModel18animals.newInstance(context)

        // Creates inputs for reference.
        val tfImage = TensorImage.fromBitmap(rotatedBitmap)

        // Runs model inference and gets result.
        val outputs = model.process(tfImage)
        val probability = outputs.probabilityAsCategoryList

        var tmpScore = probability[0].score
        var animalIndex = 0
        for (i in 0 until probability.size){
//            result += probability[i].label + ": " + probability[i].score + "\n"
            if (tmpScore < probability[i].score){
                tmpScore = probability[i].score
                animalIndex = i
            }
        }



        /*
        val model = EfficientnetLite4Int82.newInstance(context)
        val tfImage = TensorImage.fromBitmap(rotatedBitmap)
        val outputs = model.process(tfImage)

        val probability = outputs.probabilityAsCategoryList
        var tmpScore = probability[0].score
        var animalIndex = 0
        for (i in 0..999){
            if (tmpScore < probability[i].score){
                tmpScore = probability[i].score
                animalIndex = i
            }
        }
        result = probability[animalIndex].label + " " + probability[animalIndex].score

         */



//        val model = Velbloud.newInstance(context)
//
//// Creates inputs for reference.
//        val tfImage = TensorImage.fromBitmap(rotatedBitmap)
//
//// Runs model inference and gets result.
//        val outputs = model.process(tfImage)
//        val probability = outputs.probabilityAsCategoryList
//
//        result = probability[0].label + "  " + probability[0].score
//// Releases model resources if no longer used.
////        model.close()




//        val model = Model2.newInstance(context)
//
//// Creates inputs for reference.
//        val tfImage = TensorImage.fromBitmap(rotatedBitmap)
//
//// Runs model inference and gets result.
//        val outputs = model.process(tfImage)
//        val detectionResult = outputs.detectionResultList[0]
//
//// Gets result from DetectionResult.
//        val location = detectionResult.locationAsRectF;
//        val category = detectionResult.categoryAsString;
//        val score = detectionResult.scoreAsFloat;
//
//        if (score > 0.2f){
//            result = category
//        }
//        result = location.toString() + "\n" + category + "\n" + score

//        val model = Zkouska.newInstance(context)
//
//// Creates inputs for reference.
//        val tfImage = TensorImage.fromBitmap(rotatedBitmap)
//
//// Runs model inference and gets result.
//        val outputs = model.process(tfImage)
//        val first = outputs.detectionResultList.get(0)
//        val second = outputs.detectionResultList.get(1)
//// Gets result from DetectionResult.
//        val location = listOf(first.locationAsRectF, second.locationAsRectF)
//        val category = listOf(first.categoryAsString, second.categoryAsString)
//        val score = listOf(first.scoreAsFloat, second.scoreAsFloat)
//
//        result = category.toString() + "\n" + location.toString() + "\n" + score.toString()

//        val model = AutoModel100original.newInstance(context)
//        // Creates inputs for reference.
//        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 280, 280, 3), DataType.FLOAT32)
//        val tfImage = TensorImage(DataType.FLOAT32)
//        tfImage.load(rotatedBitmap)
//        val byteBuffer = tfImage.buffer
//        inputFeature0.loadBuffer(byteBuffer)
//        // Runs model inference and gets result.
//        val outputs = model.process(inputFeature0)
//        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//        model.close()
//        val probability = outputFeature0.floatArray
//        result = "Dog: " + probability[0] + "\nElephant: " + probability[1]


//        val model = Model.newInstance(context)
//
//        // Creates inputs for reference.
//        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
//
//        val tfImage = TensorImage(DataType.UINT8)
//        tfImage.load(rotatedBitmap)
//        val byteBuffer = tfImage.buffer
//
//        inputFeature0.loadBuffer(byteBuffer)
//
//        // Runs model inference and gets result.
//        val outputs = model.process(inputFeature0)
//        val probabilities = outputs.outputFeature0AsTensorBuffer.floatArray
//
//        var tmpScore = probabilities[0]
//        var animalIndex = 0
//        for (i in 0..8){
//            if (tmpScore < probabilities[i]){
//                tmpScore = probabilities[i]
//                animalIndex = i
//            }
//        }

        // Releases model resources if no longer used.
        model.close()
        imageProxy.close()


        if (tmpScore > 0.75f){
            result = probability[animalIndex].label
        }


//        , listOf(location.left, location.top, location.right, location.bottom)
        return listOf(result, rotatedBitmap)
    }


    @SuppressLint("UnsafeOptInUsageError")
    fun classifyPhoto(bitmap: Bitmap): Animal? {
        var result = ""
        val btm: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val resizedBitmap =
            Bitmap.createScaledBitmap(
                btm,
                280,
                280,
                false
            )

        val model = AutoModel18animals.newInstance(context)
        val tfImage = TensorImage.fromBitmap(resizedBitmap)
        val outputs = model.process(tfImage)
        model.close()

        val probability = outputs.probabilityAsCategoryList
        var tmpScore = probability[0].score
        var animalIndex = 0
        for (i in 0 until probability.size){
            if (tmpScore < probability[i].score){
                tmpScore = probability[i].score
                animalIndex = i
            }
        }

        if (tmpScore > 0.45f){
            result = probability[animalIndex].label
        }

        return getAnimalByName(result)
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun detectPhoto(bitmap: Bitmap): Animal? {
        var result = ""
        val btm: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val resizedBitmap =
            Bitmap.createScaledBitmap(
                btm,
                480,
                480,
                false
            )

        val options = ObjectDetectorOptions.builder()
            .setBaseOptions(BaseOptions.builder().build())
            .setMaxResults(1)
            .build()
        val objectDetector = ObjectDetector.createFromFileAndOptions(
//            context, "lite-model_efficientdet_lite3_detection_metadata_1.tflite", options
            context,  "ssddetect_OPS_metadata_6.tflite", options
        )

        val tfImage = TensorImage.fromBitmap(resizedBitmap)
        val results: List<Detection> = objectDetector.detect(tfImage)

        if (results.isNotEmpty()){
            if (results[0].categories[0].score > 0.45f) {
                result = results[0].categories[0].label
            }
        }


        Log.d("NEVIM", "LALALA: " + result)

        return getAnimalByName(result)
    }
}