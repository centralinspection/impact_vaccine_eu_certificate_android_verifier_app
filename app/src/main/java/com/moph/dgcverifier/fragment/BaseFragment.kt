package com.owner.dgcverifier.fragment

import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {
    open fun onBackPressed(): Boolean {
        return false
    }
}