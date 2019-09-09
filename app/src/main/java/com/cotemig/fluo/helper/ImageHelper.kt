package com.cotemig.fluo.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import org.jetbrains.annotations.Nullable
import java.io.*

class ImageHelper {

    companion object {

        private val IMAGE_DIRECTORY = "/br.com.fluo.fluo/"

        fun imagePath(): String {
            val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY + "images/"
            )
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs()
            }
            return wallpaperDirectory.absolutePath
        }

        fun imagePath(filename: String): File {

            val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY
            )
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs()
            }

            val f = File(wallpaperDirectory, filename.plus(".png"))

            Log.i("xxx", f.absolutePath)

            return f

        }

        fun saveImage(context: Context, myBitmap: Bitmap, filename: String): String {

            var absolutePath = ""

            val bytes = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)

            try {

                var f = File(filename)
                f.createNewFile()

                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(f.path),
                    arrayOf("image/png"), null
                )
                fo.close()

                absolutePath = f.absolutePath

                return f.absolutePath
            } catch (e1: IOException) {
                e1.printStackTrace()
            }

            return absolutePath
        }

        fun cropToSquare(bitmap: Bitmap): Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val newWidth = if (height > width) width else height
            val newHeight = if (height > width) height - (height - width) else height
            var cropW = (width - height) / 2
            cropW = if (cropW < 0) 0 else cropW
            var cropH = (height - width) / 2
            cropH = if (cropH < 0) 0 else cropH

            var bmp = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight)
            return resizeBitmap(bmp, 1000, 1000)
        }

        private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
            return Bitmap.createScaledBitmap(
                bitmap,
                width,
                height,
                false
            )
        }

        fun normalizeImageForUri(context: Context, uri: Uri, rotate: Boolean) {
            try {
                Log.d("Fluo", "URI value = " + uri.path)
                var exif = ExifInterface(uri.getPath())
                Log.d("Fluo", "Exif value = $exif")
                var orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )
                if (rotate) {
                    orientation = 6
                }
                var bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                var rotatedBitmap = rotateBitmap(bitmap, orientation)
                if (bitmap != rotatedBitmap) {
                    saveBitmapToFile(context, rotatedBitmap, uri)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_NORMAL -> return bitmap
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
                else -> return bitmap
            }
            try {
                val bmRotated =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                bitmap.recycle()

                return bmRotated
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                return null
            }

        }

        private fun saveBitmapToFile(context: Context, croppedImage: Bitmap?, saveUri: Uri?) {
            if (saveUri != null) {
                var outputStream: OutputStream? = null
                try {
                    outputStream = context.contentResolver.openOutputStream(saveUri!!)
                    if (outputStream != null) {
                        croppedImage!!.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                    }
                } catch (e: IOException) {

                } finally {
                    closeSilently(outputStream)
                    croppedImage!!.recycle()
                }
            }
        }

        private fun closeSilently(@Nullable c: Closeable?) {
            if (c == null) {
                return
            }
            try {
                c!!.close()
            } catch (t: Throwable) {
// Do nothing
            }

        }

    }

}