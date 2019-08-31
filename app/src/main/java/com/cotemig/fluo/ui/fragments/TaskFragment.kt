package com.cotemig.fluo.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.cotemig.fluo.R
import com.cotemig.fluo.helper.SwipeRecyclerViewHelper
import com.cotemig.fluo.models.Task
import com.cotemig.fluo.services.RetrofitInitializer
import com.cotemig.fluo.ui.activities.MainActivity
import com.cotemig.fluo.ui.adapters.TaskAdapter
import kotlinx.android.synthetic.main.fragment_task.*
import retrofit2.Call
import retrofit2.Response

class TaskFragment : Fragment(), SwipeRecyclerViewHelper.Companion.SwipeRecyclerViewListener {

    //    private var projectId: String? = null
    var projectId: String = ""
    lateinit var adapter: TaskAdapter

    // Tuto dentro com companion é estatico
    companion object {

        fun newInstance(idProject: String): TaskFragment {

            var t = TaskFragment()
            t.projectId = idProject
            return t

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_task, container, false)

        getTasks()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

            projectId = it.getString("idProject")

        }

    }

    fun getTasks() {


        var s = RetrofitInitializer().serviceTask()
        var call = s.getTasks(projectId!!)

        var context = context as MainActivity

        call.enqueue(object : retrofit2.Callback<List<Task>> {

            override fun onResponse(call: Call<List<Task>>?, response: Response<List<Task>>?) {

                response?.let {

                    if (it.code() == 200) {

//                        gridProjects.layoutManager = GridLayoutManager(context, 2)
//                        gridProjects.adapter = ProjectRecyclerAdapter(context, it.body(), this@ProjectsFragment)
                        tasklist.layoutManager = LinearLayoutManager(context)

                        adapter = TaskAdapter(context, it.body())

                        tasklist.adapter = adapter

                        SwipeRecyclerViewHelper.createSwipe(context, tasklist, this@TaskFragment)

                    }

                }

            }

            override fun onFailure(call: Call<List<Task>>?, t: Throwable?) {
            }

        })


    }

    override fun removeSelectedItem(position: Int) {
        adapter.removeAt(position)

        // TODO: Implementar chamada na API para excluir a tarefa
    }

    override fun reload() {
        adapter.reload()
    }
}
