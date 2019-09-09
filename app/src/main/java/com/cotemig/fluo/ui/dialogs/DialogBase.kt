package br.com.fluo.fluo.ui.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.cotemig.fluo.R

abstract class DialogBase : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent)
//        setStyle(R.style.PauseDialog, android.R.style.Theme_Translucent)
        isCancelable = false
    }
}