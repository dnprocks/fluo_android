package com.cotemig.fluo.services

import com.cotemig.fluo.models.Account
import com.cotemig.fluo.models.Project
import com.cotemig.fluo.models.Task
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServiceTask {

//    @POST("task")
//    fun createTask(@Body task: Task): Call<Task>

    @GET("project/{idProject}/tasks")
    fun getTasks(@Path("idProject") idProject: String) : Call<List<Task>>

    @GET("account/tasks")
    fun getMyTasks(): Call<List<Task>>

    @POST("task")
    fun newTask(@Body task: Task): Call<Void>

}