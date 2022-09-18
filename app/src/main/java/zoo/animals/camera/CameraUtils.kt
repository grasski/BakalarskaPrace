package zoo.animals.camera

import android.content.Context
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


object CameraUtils {
    fun getImageRotation(image: ImageProxy): Int {
        val rotation = image.imageInfo.rotationDegrees
        return rotation / 90
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }


    fun uriToBitmap(uri: Uri, context: Context): Bitmap {
        if (Build.VERSION.SDK_INT < 28) {
            return MediaStore.Images
                .Media.getBitmap(context.contentResolver, uri)

        } else {
            val source = ImageDecoder
                    .createSource(context.contentResolver, uri)

            return ImageDecoder.decodeBitmap(source)
        }

    }

    fun toBitmap(image: Image): Bitmap? {
        val planes = image.planes
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        //U and V are swapped
        yBuffer[nv21, 0, ySize]
        vBuffer[nv21, ySize, vSize]
        uBuffer[nv21, ySize + vSize, uSize]
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    fun toBitmap2(image: Image): Bitmap? {
        val yBuffer = image.planes[0].buffer // Y
        val vuBuffer = image.planes[2].buffer // VU

        val ySize = yBuffer.remaining()
        val vuSize = vuBuffer.remaining()

        val nv21 = ByteArray(ySize + vuSize)

        yBuffer.get(nv21, 0, ySize)
        vuBuffer.get(nv21, ySize, vuSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun yuv420ToBitmap(image: Image): Bitmap? {
        val width = image.width
        val height = image.height
        // Pass this array to native code to write to.
        val argbResult: IntArray = IntArray(width * height)
        val yPlane = image.planes[0]
        val uPlane = image.planes[1]
        val vPlane = image.planes[2]
        val yBuffer = yPlane.buffer
        val uBuffer = uPlane.buffer
        val vBuffer = vPlane.buffer

        // call the JNI method
        yuv420ToBitmapNative(
            width,
            height,
            yBuffer,
            yPlane.pixelStride,
            yPlane.rowStride,
            uBuffer,
            uPlane.pixelStride,
            uPlane.rowStride,
            vBuffer,
            vPlane.pixelStride,
            vPlane.rowStride,
            argbResult
        )

        return Bitmap.createBitmap(argbResult, width, height, Bitmap.Config.ARGB_8888)
    }

    // native interface
    external fun yuv420ToBitmapNative(
        width: Int,
        height: Int,
        yBuffer: ByteBuffer?,
        yPixelStride: Int,
        yRowStride: Int,
        uBuffer: ByteBuffer?,
        uPixelStride: Int,
        uRowStride: Int,
        vBuffer: ByteBuffer?,
        vPixelStride: Int,
        vRowStride: Int,
        argbResult: IntArray?
    )
}