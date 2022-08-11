package zoo.animals.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import zoo.animals.ml.ModelNewAnimals

class ImageClassifier(private val context: Context) {
    @SuppressLint("UnsafeOptInUsageError")
    fun classify(imageProxy: ImageProxy): List<Any?> {
        val animalLabels: MutableList<String> = FileUtil.loadLabels(context, "labels.txt")

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


        val model = ModelNewAnimals.newInstance(context)

        // Creates inputs for reference.
        val tfImage = TensorImage.fromBitmap(rotatedBitmap)

        // Runs model inference and gets result.
        val outputs = model.process(tfImage)
        val probability = outputs.probabilityAsCategoryList

        var tmpScore = probability[0].score
        var animalIndex = 0
        for (i in 0 until animalLabels.size){
//            result += probability[i].label + ": " + probability[i].score + "\n"
            if (tmpScore < probability[i].score){
                tmpScore = probability[i].score
                animalIndex = i
            }
        }


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

        return listOf(result, rotatedBitmap)
    }
}