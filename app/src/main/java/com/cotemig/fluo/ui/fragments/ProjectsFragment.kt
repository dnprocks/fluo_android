package com.cotemig.fluo.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.cotemig.fluo.R
import com.cotemig.fluo.models.Project
import com.cotemig.fluo.services.RetrofitInitializer
import com.cotemig.fluo.ui.activities.MainActivity
import com.cotemig.fluo.ui.adapters.ProjectRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_projects.*
import retrofit2.Call
import retrofit2.Response


class ProjectsFragment : Fragment(), ProjectRecyclerAdapter.ItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_projects, container, false)

        getProject()

        return view
    }

    fun getProject() {

        var s = RetrofitInitializer().serviceProject()
        var call = s.getProjects()

        var context = context as MainActivity

        call.enqueue(object : retrofit2.Callback<List<Project>> {

            override fun onResponse(call: Call<List<Project>>?, response: Response<List<Project>>?) {

                response?.let {

                    if (it.code() == 200) {


                        if(it.body().size > 0) {

                            gridProjects.layoutManager = GridLayoutManager(context, 2)
                            gridProjects.adapter = ProjectRecyclerAdapter(context, it.body(), this@ProjectsFragment)

                        } else {

                            context.setFragment(NoTaskFragment())

                        }

                    }

                }

            }

            override fun onFailure(call: Call<List<Project>>?, t: Throwable?) {
            }

        })
    }

    override fun onItemClick(project: Project, position: Int) {
        Toast.makeText((context as MainActivity), project.name, Toast.LENGTH_LONG).show()

        if(project.tasks > 0){
            (context as MainActivity).setFragmentAndAddToBackstack(TaskFragment.newInstance(project.id))
        }else{
            (context as MainActivity).setFragmentAndAddToBackstack(NoTaskFragment())
        }

//        var bundle = Bundle()
//        bundle.putString("idProject", project.id)
//
//        var taskFragment = TaskFragment()
//        taskFragment.arguments = bundle
//        (context as MainActivity).setFragment(taskFragment)


    }
}
