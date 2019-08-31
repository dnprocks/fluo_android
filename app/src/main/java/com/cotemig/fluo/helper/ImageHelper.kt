package com.cotemig.fluo.helper

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import java.io.*

class ImageHelper {

    companion object {

        private val IMAGE_DIRECTORY = "/com.cotemig.fluo/"

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


    }

}