package com.cotemig.fluo.app

import android.app.Application
import com.cotemig.fluo.models.Account

class FluoApp: Application {

    constructor() : super()

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    object Directories {
        var IMAGES = "images"

    }

    object FileProvider {
        var authority = "br.com.fluo.fluo.files.provider.authority.1.x.1.1.abc.xyz_xpna"
    }

    companion object {

        //https://github.com/dirceubelem/task-api

        val TYPE_IMAGE = ".png"
        var URL_IMAGE = "https://file.fluo.site/"

        var account: Account? = null

        val PROD = false
        val URL_API_PROD = "https://api.fluo.site/v1/"
        val URL_API_DEV = "https://api.fluo.site/v1/"

        val URL_API = if(PROD) URL_API_PROD else URL_API_DEV

        val DATABASE_PATH = "/data/data/com.cotemig.fluo/databases"
        val DATABASE_NAME = "task.sqlite"
        val ABSOLUTE_DATABASE_PATH = (DATABASE_PATH.plus("/").plus(DATABASE_NAME))
        val DATABASE_PATH_TEMP = "/data/data/com.cotemig.fluo/temp"
        val CURRENT_REVISION = 2

        val PATH_APP_TEMP = "/sdcard/fluo/"
        val DATABASE_NAME_TEMP = "task.zip"
        val PATH_APP = "/sdcard/fluo/"
        val ASSETS_DATABASE_PATH = "db"
        val DATABASE_UPDATE = "update.sqlite"

        private var instance: FluoApp? = null

        fun getInstance(): FluoApp? {
            return instance
        }

    }
}