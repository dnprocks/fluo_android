package com.cotemig.fluo.database.model

import android.database.Cursor
import com.cotemig.fluo.database.fw.Column
import com.cotemig.fluo.database.fw.TOBase
import com.cotemig.fluo.database.fw.Table

@Table(name = "task")
class TOTask : TOBase(){

    @Column(name = "id", jsonName = "id")
    var id: String? = null

    @Column(name = "name", jsonName = "name")
    var name: String? = null

    @Column(name = "idProject", jsonName = "idProject")
    var idProject: String? = null

    @Column(name = "idAccountTo", jsonName = "idAccountTo")
    var idAccountTo: String?= null

    @Column(name = "description", jsonName = "description")
    var description: String? = null

    @Column(name = "tags", jsonName = "tags")
    var tags: String? = null

    @Column(name = "createdAt", jsonName = "createdAt")
    var createdAt: Float? = null

    @Column(name = "priority", jsonName = "priority")
    var priority: Float? = null

    override fun loadManual(cursor: Cursor) {

        id = cursor.getString(0)
        name = cursor.getString(1)
        idProject = cursor.getString(2)
        idAccountTo = cursor.getString(3)
        description = cursor.getString(4)
        tags = cursor.getString(5)
        createdAt = cursor.getFloat(6)
        priority = cursor.getFloat(7)
    }

}