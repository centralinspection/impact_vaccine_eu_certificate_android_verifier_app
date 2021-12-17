package com.owner.dgcverifier.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.owner.dgcverifier.R
import com.owner.dgcverifier.databinding.FragmentMenuDialogBinding

class MenuDialogFragment: DialogFragment() {
    companion object {
        val tag = MenuDialogFragment::class.java.simpleName
        fun newInstance() = MenuDialogFragment()
    }

    private var binding: FragmentMenuDialogBinding? = null
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
        binding = FragmentMenuDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.aboutUsRow?.setOnClickListener {
            listener?.goToAboutUs()
            dialog?.dismiss()
        }

        binding?.termsConditionsRow?.setOnClickListener {
            listener?.goToTermsAndConditions()
            dialog?.dismiss()
        }

        binding?.privacyRow?.setOnClickListener {
            listener?.goToPrivacyPolicy()
            dialog?.dismiss()
        }

        binding?.verifierRow?.setOnClickListener {
            listener?.goToVerifier()
            dialog?.dismiss()
        }

        binding?.background?.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDialogInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnDialogInteractionListener {
        fun goToAboutUs()
        fun goToTermsAndConditions()
        fun goToPrivacyPolicy()
        fun goToVerifier()
    }
}