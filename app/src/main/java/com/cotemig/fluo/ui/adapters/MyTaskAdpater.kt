package com.cotemig.fluo.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cotemig.fluo.R
import com.cotemig.fluo.helper.DateTime
import com.cotemig.fluo.models.Task
import kotlinx.android.synthetic.main.item_my_task.view.*
import java.util.*

class MyTaskAdpater(var context: Context, var list: List<Task>) :
    RecyclerView.Adapter<MyTaskAdpater.MyTaskViewHolder>() {

    var listTask: ArrayList<Task> = list as ArrayList<Task>

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyTaskAdpater.MyTaskViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_my_task, p0, false)
        return MyTaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: MyTaskAdpater.MyTaskViewHolder, p1: Int) {
        p0.bind(context, list[p1], p1)
    }

    fun filter(text: String){

        var listFilter = ArrayList<Task>()

        listTask.forEach{
            if(it.name.toLowerCase().contains(text.toLowerCase())){
                listFilter.add(it)
            }
        }

        list = listFilter

        notifyDataSetChanged()

    }

    class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name = itemView.name
        var time = itemView.time

        fun bind(context: Context, item: Task, p: Int) {

            name.text = item.name

            var date = DateTime(item.createdAt, "dd/MM/yyyy HH:mm:ss")

            time.text = date.toString("hh:mm a")

        }
    }
}