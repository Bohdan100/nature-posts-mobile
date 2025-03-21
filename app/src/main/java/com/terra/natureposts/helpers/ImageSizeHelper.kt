package com.terra.natureposts.helpers

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
import androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

import com.terra.natureposts.ui.PostDetailsActivity

object ImageSizeHelper {
    fun loadImage(context: Context, imageUrl: String, imageView: SubsamplingScaleImageView) {
        Thread {
            try {
                val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
                val inputStream: InputStream = URL(imageUrl).openStream()
                val outputStream = FileOutputStream(tempFile)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                val exif = ExifInterface(tempFile.absolutePath)
                val orientation = exif.getAttributeInt(
                    TAG_ORIENTATION,
                    ORIENTATION_NORMAL
                )

                val rotation = when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270
                    else -> 0
                }

                val imageUri = Uri.fromFile(tempFile)
                (context as? PostDetailsActivity)?.runOnUiThread {
                    imageView.orientation = rotation
                    imageView.setImage(ImageSource.uri(imageUri))

                    imageView.maxScale = 10f
                    imageView.minScale = 1f
                    imageView.setMinimumTileDpi(160)
                    imageView.setPanEnabled(true)
                    imageView.isZoomEnabled = true
                    imageView.isQuickScaleEnabled = true
                    imageView.setDoubleTapZoomDuration(200)
                    imageView.setDoubleTapZoomScale(2f)
                    imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                (context as? PostDetailsActivity)?.runOnUiThread {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}