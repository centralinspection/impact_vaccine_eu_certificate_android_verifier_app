package com.owner.dgcverifier.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.owner.dgcverifier.FragmentInteractionListener
import com.owner.dgcverifier.adapters.VaccinesRecyclerViewAdapter
import com.owner.dgcverifier.databinding.FragmentScanSuccessBinding
import dgca.verifier.app.decoder.model.GreenCertificate

class ScanSuccessFragment : BaseFragment() {
    companion object {
        val tag = ScanSuccessFragment::class.java.simpleName
        fun newInstance(certificate: GreenCertificate): ScanSuccessFragment {
            val fragment = ScanSuccessFragment()
            val args = Bundle()
            args.putSerializable(KEY_CERTIFICATE_INFO, certificate)
            fragment.arguments = args
            return fragment
        }

        const val KEY_CERTIFICATE_INFO = "CERTIFICATE_INFO"
    }

    private var _binding: FragmentScanSuccessBinding? = null
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
        _binding = FragmentScanSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val certificate = arguments?.getSerializable(KEY_CERTIFICATE_INFO) as GreenCertificate
        val adapter = VaccinesRecyclerViewAdapter(certificate)
        binding.scanSuccessRecyclerView.adapter = adapter

        binding.headerLayout.settingsOverflowButton.setOnClickListener{
            listener?.openMenu()
        }

        binding.backHomeButton.setOnClickListener {
            listener?.loadFragment(HomeFragment.newInstance(), HomeFragment.tag, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}