package com.owner.dgcverifier.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.owner.dgcverifier.FragmentInteractionListener
import com.owner.dgcverifier.databinding.FragmentPrivacyPolicyBinding

class PrivacyPolicyFragment: BaseFragment() {
    companion object {
        val tag = PrivacyPolicyFragment::class.java.simpleName
        fun newInstance() = PrivacyPolicyFragment()
    }

    private var _binding: FragmentPrivacyPolicyBinding? = null
    var listener: FragmentInteractionListener? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.headerLayout.settingsOverflowButton.setOnClickListener{
            listener?.openMenu()
        }

        binding.backHomeButton.setOnClickListener {
            listener?.loadFragment(HomeFragment.newInstance(), HomeFragment.tag, false)
        }

        binding.ppContactEmailAr.setOnClickListener {
            listener?.sendEmail()
        }

        binding.ppContactEmailEn.setOnClickListener {
            listener?.sendEmail()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}