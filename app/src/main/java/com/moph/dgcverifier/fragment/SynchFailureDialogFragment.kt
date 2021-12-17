package com.owner.dgcverifier.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.owner.dgcverifier.R
import com.owner.dgcverifier.databinding.FragmentSynchFailureDialogBinding

class SynchFailureDialogFragment: DialogFragment() {
    companion object {
        val tag = SynchFailureDialogFragment::class.java.simpleName
        fun newInstance() = SynchFailureDialogFragment()
    }

    private var binding: FragmentSynchFailureDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DimmedFullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSynchFailureDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.okButton?.setOnClickListener {
            dialog?.dismiss()
        }
    }
}