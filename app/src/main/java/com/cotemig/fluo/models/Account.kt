package com.cotemig.fluo.models

import org.json.JSONObject

class Account{

    var id: String = ""
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var token: String = ""

    fun getJSON(): JSONObject{
        var json = JSONObject()
        json.put("email", email)
        json.put("password", password)
        return json
    }

    fun parseJSON(json: String){
        var js =  JSONObject(json)
        email = js.getString("email")
        password = js.getString("password")
    }
}