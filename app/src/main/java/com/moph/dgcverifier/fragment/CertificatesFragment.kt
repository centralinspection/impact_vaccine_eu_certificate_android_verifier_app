package com.owner.dgcverifier.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.owner.dgcverifier.DGCCertificateManager
import com.owner.dgcverifier.FragmentInteractionListener
import com.owner.dgcverifier.databinding.FragmentCertificatesBinding
import com.owner.dgcverifier.viewmodels.CertificatesFragmentViewModel

class CertificatesFragment : BaseFragment() {
    companion object {
        val tag = CertificatesFragment::class.java.simpleName
        fun newInstance() = CertificatesFragment()
    }

    private var _binding: FragmentCertificatesBinding? = null
    var listener: FragmentInteractionListener? = null
    private val viewModel by viewModels<CertificatesFragmentViewModel>()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.synchComplete.observe(this, {
            displaySynchDate()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCertificatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.synchronizeButton.setOnClickListener {
            synchronize()
        }

        binding.headerLayout.settingsOverflowButton.setOnClickListener{
            listener?.openMenu()
        }

        binding.backHomeButton.setOnClickListener {
            listener?.loadFragment(HomeFragment.newInstance(), HomeFragment.tag, false)
        }

        displaySynchDate()
        binding.validScansValue.text = DGCCertificateManager.successfulScans.toString()
        binding.invalidScansValue.text = DGCCertificateManager.failedScans.toString()
    }

    private fun synchronize() {
        binding.synchronizeButton.visibility = View.INVISIBLE
        binding.synchLoader.visibility = View.VISIBLE

        listener?.fetchCertificates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displaySynchDate() {
        binding.synchronizeButton.visibility = View.VISIBLE
        binding.synchLoader.visibility = View.INVISIBLE

        val dateString = DGCCertificateManager.lastSyncDate
        binding.certificateDateValueAr.text = dateString
        binding.certificateDateValueEn.text = dateString
    }
}