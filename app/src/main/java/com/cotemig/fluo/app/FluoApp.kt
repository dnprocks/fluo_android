package com.cotemig.fluo.app

import android.app.Application
import com.cotemig.fluo.models.Account

class FluoApp: Application {

    constructor() : super()

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        //https://github.com/dirceubelem/task-api

        var account: Account? = null

        val PROD = false
        val URL_API_PROD = "https://api.fluo.site/v1/"
        val URL_API_DEV = "https://api.fluo.site/v1/"

        val URL_API = if(PROD) URL_API_PROD else URL_API_DEV

        private var instance: FluoApp? = null

        fun getInstance(): FluoApp? {
            return instance
        }

    }
}