package com.owner.dgcverifier

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.owner.dgcverifier.DGCCertificateManager.synchIsNeeded
import com.owner.dgcverifier.databinding.ActivityMainBinding
import com.owner.dgcverifier.fragment.*
import com.owner.dgcverifier.viewmodels.MainActivityViewModel


class MainActivity : AppCompatActivity(),
    FragmentInteractionListener,
    MenuDialogFragment.OnDialogInteractionListener {
    private lateinit var binding: ActivityMainBinding
    private var currentFragment: BaseFragment? = null

    private val viewModel by viewModels<MainActivityViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fetchCertificatesResponse.observe(this, { fetchSucceeded ->
            if (!fetchSucceeded) {
                displaySynchFailure()
            }
        })

        loadFragment(HomeFragment.newInstance(), HomeFragment.tag, false)

        if (synchIsNeeded()) {
            fetchCertificates()
        }
    }

    override fun fetchCertificates() {
        viewModel.fetchCertificates()
    }

    override fun sendEmail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email)))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onBackPressed() {
        if (currentFragment != null) {
            val isBackpressHandled = (currentFragment as BaseFragment).onBackPressed()
            if (!isBackpressHandled) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun loadFragment(fragment: BaseFragment, tag: String, isAddToBackStack: Boolean) {
        if (currentFragment != null && tag == currentFragment?.tag) {
            return
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag)

        currentFragment = fragment
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(null)
        } else {
            val backStackCount = supportFragmentManager.backStackEntryCount
            for (i in 0 until backStackCount) {
                supportFragmentManager.popBackStackImmediate()
            }
        }
        fragmentTransaction.commit()
    }

    override fun openMenu() {
        val fragment = MenuDialogFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(fragment, MenuDialogFragment.tag)
            .commitAllowingStateLoss()
    }

    override fun goToAboutUs() {
        loadFragment(AboutUsFragment.newInstance(), AboutUsFragment.tag, true)
    }

    override fun goToTermsAndConditions() {
        loadFragment(TermsAndConditionsFragment.newInstance(), TermsAndConditionsFragment.tag, true)
    }

    override fun goToPrivacyPolicy() {
        loadFragment(PrivacyPolicyFragment.newInstance(), PrivacyPolicyFragment.tag, true)
    }

    override fun goToVerifier() {
        loadFragment(CertificatesFragment.newInstance(), CertificatesFragment.tag, true)
    }

    private fun displaySynchFailure() {
        val fragment = SynchFailureDialogFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(fragment, SynchFailureDialogFragment.tag)
            .commitAllowingStateLoss()
    }
}