package com.cotemig.fluo.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.cotemig.fluo.R
import com.cotemig.fluo.models.Project
import com.cotemig.fluo.models.Task
import com.cotemig.fluo.services.RetrofitInitializer
import com.cotemig.fluo.ui.activities.MainActivity
import com.cotemig.fluo.ui.adapters.MyTaskAdpater
import com.cotemig.fluo.ui.adapters.ProjectRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_my_task.*
import kotlinx.android.synthetic.main.fragment_projects.*
import retrofit2.Call
import retrofit2.Response


class MyTaskFragment : Fragment() {

    lateinit var adapter: MyTaskAdpater

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_my_task, container, false)

        getMyTasks()

        var search = view.findViewById<SearchView>(R.id.search)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(text: String?): Boolean {

                adapter.filter(text!!)

                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {

                adapter.filter(text!!)

                return false
            }

        })

        return view
    }

    fun getMyTasks() {

        var s = RetrofitInitializer().serviceTask()
        var call = s.getMyTasks()

        var context = context as MainActivity

        call.enqueue(object : retrofit2.Callback<List<Task>> {

            override fun onResponse(call: Call<List<Task>>?, response: Response<List<Task>>?) {

                response?.let {

                    if (it.code() == 200) {

//                        gridProjects.layoutManager = GridLayoutManager(context, 2)
//                        gridProjects.adapter = ProjectRecyclerAdapter(context, it.body(), this@ProjectsFragment)

                        if (it.body().size > 0) {

                            myTasks.layoutManager = LinearLayoutManager(context)

                            adapter = MyTaskAdpater(context, it.body())//TaskAdapter(context, it.body())

                            myTasks.adapter = adapter
                        } else {

                            context.setFragment(NoTaskFragment())

                        }

                    }

                }

            }

            override fun onFailure(call: Call<List<Task>>?, t: Throwable?) {
            }

        })


    }

}
