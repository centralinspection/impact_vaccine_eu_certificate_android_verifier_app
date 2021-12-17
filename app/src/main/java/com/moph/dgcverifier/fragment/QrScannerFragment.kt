package com.owner.dgcverifier.fragment

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.owner.dgcverifier.DGCCertificateManager
import com.owner.dgcverifier.FragmentInteractionListener
import com.owner.dgcverifier.QrDecoder
import com.owner.dgcverifier.databinding.FragmentQrScannerBinding
import com.owner.dgcverifier.fragment.ScanSuccessFragment.Companion.KEY_CERTIFICATE_INFO
import dgca.verifier.app.decoder.CertificateDecodingResult
import dgca.verifier.app.decoder.base45.Base45Decoder
import me.dm7.barcodescanner.zxing.ZXingScannerView


class QrScannerFragment: BaseFragment(),
    ScanFailureDialogFragment.OnDialogInteractionListener,
    ZXingScannerView.ResultHandler {
    companion object {
        val tag = QrScannerFragment::class.java.simpleName
        fun newInstance() = QrScannerFragment()
    }

    private var binding: FragmentQrScannerBinding? = null
    var listener: FragmentInteractionListener? = null
    var timer: CountDownTimer = object : CountDownTimer(30000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() { activity?.onBackPressed() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentQrScannerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissionsAndStartCamera()
        timer.start()

        binding?.closeButton?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

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

    private fun checkPermissionsAndStartCamera() {
        if (listener?.hasPermission(Manifest.permission.CAMERA) == true) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            activity?.onBackPressed()
        }
    }

    private fun startCamera() {
        binding?.zxingCameraPreview?.setFormats(listOf(BarcodeFormat.QR_CODE))
        binding?.zxingCameraPreview?.setAspectTolerance(0.5f) //For Huawei compatibility
        binding?.zxingCameraPreview?.setBorderAlpha(0f)
        binding?.zxingCameraPreview?.setLaserEnabled(false)
        binding?.zxingCameraPreview?.setResultHandler(this)
        context?.let {
            binding?.zxingCameraPreview?.setMaskColor(
                ContextCompat.getColor(it, android.R.color.transparent))
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.zxingCameraPreview?.startCamera()
    }

    override fun onPause() {
        super.onPause()
        binding?.zxingCameraPreview?.stopCamera()
    }

    override fun handleResult(result: Result?) {
        val qrText = result?.text.orEmpty()
        val certDecoder= QrDecoder(Base45Decoder())
        val decodedCertificate=certDecoder.decodeCertificate(qrText)
        if (decodedCertificate is CertificateDecodingResult.Success ){
            val certificate=decodedCertificate.greenCertificate
            val args = Bundle()
            args.putSerializable(KEY_CERTIFICATE_INFO, certificate)
            DGCCertificateManager.successfulScans++
            listener?.loadFragment(
                ScanSuccessFragment.newInstance(certificate),
                ScanSuccessFragment.tag, true)
        } else {
            DGCCertificateManager.failedScans++
            val fragment = ScanFailureDialogFragment.newInstance()
            childFragmentManager.beginTransaction()
                .add(fragment, ScanFailureDialogFragment.tag)
                .commitAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        timer.cancel()
    }

    override fun scanAgain() {
        startCamera()
        binding?.zxingCameraPreview?.startCamera()
    }

    override fun closeScan() {
        activity?.onBackPressed()
    }
}