package com.xently.ui.demo.ui.dialog.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xently.dialog.ButtonText
import com.xently.ui.demo.R
import com.xently.xui.dialog.DialogFragment

class CustomDialog : DialogFragment() {

    /*override val dialogContentFromResource: Int?
        get() = R.layout.activity_main*/

    override fun setDialogButtonText(context: Context): ButtonText? {
        return super.setDialogButtonText(context)?.copy("Positive", "Negative")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.custom, container, false)
}