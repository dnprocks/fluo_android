package com.cotemig.fluo.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.fluo.fluo.ui.dialogs.DialogBase
import com.cotemig.fluo.R

class AddTaskDialog: DialogBase() {

    companion object {

        fun getDialog(): AddTaskDialog{
            val dialog = AddTaskDialog()
            return dialog
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.dialog_add_task, container, false)






        return view
    }
}