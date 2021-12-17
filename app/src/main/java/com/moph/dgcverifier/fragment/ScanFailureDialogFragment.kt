package com.owner.dgcverifier.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.owner.dgcverifier.R
import com.owner.dgcverifier.databinding.FragmentScanFailureDialogBinding

class ScanFailureDialogFragment : DialogFragment() {
    companion object {
        val tag = ScanFailureDialogFragment::class.java.simpleName
        fun newInstance() = ScanFailureDialogFragment()
    }

    private var binding: FragmentScanFailureDialogBinding? = null
    var listener: OnDialogInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DimmedFullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanFailureDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        binding?.rescanButton?.setOnClickListener {
            listener?.scanAgain()
            dialog?.dismiss()
        }

        binding?.backButton?.setOnClickListener {
            listener?.closeScan()
            dialog?.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnDialogInteractionListener) {
            listener = parentFragment as OnDialogInteractionListener
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnDialogInteractionListener {
        fun scanAgain()
        fun closeScan()
    }
}