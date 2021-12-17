package com.owner.dgcverifier

import com.owner.dgcverifier.fragment.BaseFragment

interface FragmentInteractionListener {
    fun hasPermission(permission: String): Boolean
    fun openMenu()
    fun fetchCertificates()
    fun loadFragment(fragment: BaseFragment, tag: String, isAddToBackStack: Boolean)
    fun sendEmail()
}