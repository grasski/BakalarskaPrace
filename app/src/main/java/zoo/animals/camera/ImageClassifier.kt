package zoo.animals.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.compose.ui.unit.Dp
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import org.tensorflow.lite.task.vision.detector.ObjectDetector.ObjectDetectorOptions
import zoo.animals.ml.*


class ImageClassifier(private val context: Context) {

    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()

    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    @SuppressLint("UnsafeOptInUsageError")
    fun detect(imageProxy: ImageProxy): List<Any> {
        var text = ""
        var location = emptyList<Float>()

        val options = ObjectDetectorOptions.builder()
            .setBaseOptions(BaseOptions.builder().build())
            .setMaxResults(1)
            .build()
        val objectDetector = ObjectDetector.createFromFileAndOptions(
            context, "zkouska.tflite", options
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
            text = results[0].categories[0].label

            val loc = results[0].boundingBox
            location = listOf(loc.left, loc.top, loc.right, loc.bottom)
            text += "  " + location
        }

        imageProxy.close()
        Log.d("halo", "NEVIMMMMM: " + text)
        return listOf(text, location)
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



//        val model = LiteModelEfficientdetLite0DetectionMetadata1.newInstance(context)
//
//// Creates inputs for reference.
//        val tfImage = TensorImage.fromBitmap(rotatedBitmap)
//
//// Runs model inference and gets result.
//        val outputs = model.process(tfImage)
//
//        for (i in 0..2){
//            val detectionResult = outputs.detectionResultList[i]
//
//// Gets result from DetectionResult.
//            val location = detectionResult.locationAsRectF;
//            val category = detectionResult.categoryAsString;
//            val score = detectionResult.scoreAsFloat;
//
//            result += category + "  " + score + "\n"
//        }



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
}