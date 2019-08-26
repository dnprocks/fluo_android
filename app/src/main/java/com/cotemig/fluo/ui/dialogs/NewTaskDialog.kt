package br.com.fluo.fluo.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cotemig.fluo.R


class NewTaskDialog : DialogBase() {

    var listener: NewTaskDialogListener? = null

    companion object {

        fun getDialog(listener: NewTaskDialogListener): NewTaskDialog {
            val dialog = NewTaskDialog()
            dialog.listener = listener
            return dialog
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

//        var view = inflater.inflate(R.layout.new_task_dialog, container, false)

        return view

    }

    override fun onStart() {
        super.onStart()

        val decorView = dialog
            .window!!
            .decorView

        decorView.animate()
            .translationY(3000f)
            .setStartDelay(0)
            .setDuration(0)
            .start()

        decorView.animate()
            .translationY(0f)
            .setStartDelay(0)
            .setDuration(200)
            .start()

    }

    interface NewTaskDialogListener {
        fun selected(id: String, index: Int)
    }

}