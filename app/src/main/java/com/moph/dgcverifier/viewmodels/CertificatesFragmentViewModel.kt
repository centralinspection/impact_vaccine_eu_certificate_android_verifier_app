package com.owner.dgcverifier.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.owner.dgcverifier.network.RxBus
import com.owner.dgcverifier.network.SynchResultEvent
import io.reactivex.disposables.Disposable

class CertificatesFragmentViewModel: ViewModel() {
    val synchComplete: MutableLiveData<Boolean> = MutableLiveData()
    private var disposable: Disposable? = null

    init {
        disposable =  RxBus.sendEventObservable
            .ofType(SynchResultEvent::class.java)
            .subscribe {
                synchComplete.value = it.success
            }
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}