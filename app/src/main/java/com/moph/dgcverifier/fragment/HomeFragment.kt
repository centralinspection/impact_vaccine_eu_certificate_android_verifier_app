package com.owner.dgcverifier.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.owner.dgcverifier.DGCCertificateManager
import com.owner.dgcverifier.DGCCertificateManager.synchIsNeeded
import com.owner.dgcverifier.FragmentInteractionListener
import com.owner.dgcverifier.databinding.FragmentHomeBinding
import com.owner.dgcverifier.viewmodels.HomeFragmentViewModel

class HomeFragment : BaseFragment() {
    companion object {
        val tag = HomeFragment::class.java.simpleName
        fun newInstance() = HomeFragment()
    }

    private var _binding: FragmentHomeBinding? = null
    private val viewModel by viewModels<HomeFragmentViewModel>()
    private var listener: FragmentInteractionListener? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.synchComplete.observe(this, { fetchSucceeded ->
            if (!fetchSucceeded) {
                if (synchIsNeeded()) {
                    binding.certificateOutOfDate.visibility = View.VISIBLE
                } else {
                    binding.certificateOutOfDate.visibility = View.INVISIBLE
                }
            }
            displaySynchDate()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displaySynchDate()

        binding.scanButtonBorder.setOnClickListener {
            listener?.loadFragment(QrScannerFragment.newInstance(), QrScannerFragment.tag, true)
        }

        binding.certificateOutOfDate.setOnClickListener {
            listener?.loadFragment(
                CertificatesFragment.newInstance(),
                CertificatesFragment.tag,
                true
            )
        }

        binding.headerLayout.settingsOverflowButton.setOnClickListener{
            listener?.openMenu()
        }
    }

    private fun displaySynchDate() {
        val dateString = DGCCertificateManager.lastSyncDate
        binding.certificateDateValueAr.text = dateString
        binding.certificateDateValueEn.text = dateString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}