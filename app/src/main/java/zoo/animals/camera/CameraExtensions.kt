package zoo.animals.camera

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import zoo.animals.R
import zoo.animals.UiTexts
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor


class CameraExtensions {

    fun takePhoto(
        imageCapture: ImageCapture,
        executor: Executor,
        context: Context,
        onImageCaptured: (Uri) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ){
        val galleryPath = getGalleryPath(context)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            galleryPath
        ).build()

        imageCapture.takePicture(outputOptions, executor, object: ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri
                savedUri?.let { onImageCaptured(it) }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("onImageSavedError", "Error while taking picture: $exception")
                onError(exception)
            }
        })
    }

    private fun getGalleryPath(context: Context): ContentValues {
        val timestamp = SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-SSS",
            Locale.ENGLISH
        ).format(System.currentTimeMillis())

        val imgName = UiTexts.StringResource(R.string.app_name).asString(context)
            .replace(" ", "_") + "_" + timestamp

        val contentValue = ContentValues()
        contentValue.put(MediaStore.MediaColumns.DISPLAY_NAME, imgName)
        contentValue.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

        return contentValue
    }


    fun deleteImage(
        context: Context,
        uri: Uri,
        success: (Boolean) -> Unit
    ) {
        val file = uri.path?.let { File(it) }
        if (file != null){
            val delete = file.delete(context)
            success(delete)
        } else{
            success(false)
        }
    }

    private fun File.delete(context: Context): Boolean {
        var selectionArgs = arrayOf(this.absolutePath)
        val contentResolver = context.contentResolver
        lateinit var where: String
        lateinit var filesUri: Uri

        if (Build.VERSION.SDK_INT >= 29) {
            filesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            where = MediaStore.Images.Media._ID + "=?"
            selectionArgs = arrayOf(this.name)
        } else {
            where = MediaStore.MediaColumns.DATA + "=?"
            filesUri = MediaStore.Files.getContentUri("external")
        }

        contentResolver.delete(filesUri, where, selectionArgs)

        return !this.exists()
    }
}