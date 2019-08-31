package com.cotemig.fluo.helper

import android.content.Context
import android.os.Environment
import java.io.File

object SDCardHelper {

    private val TAG = SDCardHelper::class.java.name

    fun getSdCardFile(context: Context, dirName: String, fileName: String): File {
        val state = Environment.getExternalStorageState()

        var sdcard: File?
        if (Environment.MEDIA_MOUNTED == state) {
            sdcard = context.getExternalFilesDir(null)

            if (sdcard == null) {
                sdcard = context.filesDir
            }
        } else {
            sdcard = context.filesDir
        }

        val dir = File(sdcard, dirName)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return File(dir, fileName)
    }

}