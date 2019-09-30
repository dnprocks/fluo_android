package com.cotemig.fluo.models

data class Task(
    var id: String = "",
    var name: String = "",
    var idProject: String = "",
    var idAccountTo: String = "",
    var description: String = "",
    var tags: String = "",
    var createdAt: String = "",
    var priority: Int = 0
)

