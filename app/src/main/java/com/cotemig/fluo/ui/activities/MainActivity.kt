package com.cotemig.fluo.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cotemig.fluo.R
import com.cotemig.fluo.app.FluoApp
import com.cotemig.fluo.ui.fragments.MyTaskFragment
import com.cotemig.fluo.ui.fragments.NoTaskFragment
import com.cotemig.fluo.ui.fragments.ProjectsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userwelcome.text = getString(R.string.textWelcome, FluoApp.account!!.name) //        userwelcome.text = String.format(resources.getString(R.string.textWelcome, FluoApp.account!!.name))
        howManyTask.setText(R.string.textNoTasks)


        var url = "https://zcoin.io/wp-content/uploads/2017/01/blank-avatar-300x300.png"

        Glide
            .with(this)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(avatar)

//        Glide.with(this).load(url).into(avatar)

        btntask.setOnClickListener {
            setFragmentAndAddToBackstack(ProjectsFragment())
            btnhome.setColorFilter(resources.getColor(R.color.textmenuinactive))
            btntask.setColorFilter(resources.getColor(R.color.textmenu))
            tasktext.setTextColor(resources.getColor(R.color.textmenu))
            hometext.setTextColor(resources.getColor(R.color.textmenuinactive))
        }

        btnhome.setOnClickListener {
            setFragment(MyTaskFragment())
            btntask.setColorFilter(resources.getColor(R.color.textmenuinactive))
            btnhome.setColorFilter(resources.getColor(R.color.textmenu))
            hometext.setTextColor(resources.getColor(R.color.textmenu))
            tasktext.setTextColor(resources.getColor(R.color.textmenuinactive))
        }

        btnhome.callOnClick()

    }

    fun setFragment(f: Fragment) {
        var ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content, f)
        ft.commit()
    }

    fun setFragmentAndAddToBackstack(f: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.addToBackStack(null)
        ft.replace(R.id.content, f)
        ft.commit()
    }
}
