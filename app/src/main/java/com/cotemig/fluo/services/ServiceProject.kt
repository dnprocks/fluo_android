package com.cotemig.fluo.services

import com.cotemig.fluo.models.Project
import retrofit2.Call
import retrofit2.http.GET

interface ServiceProject {

    @GET("project")
    fun getProjects() : Call<List<Project>>

}