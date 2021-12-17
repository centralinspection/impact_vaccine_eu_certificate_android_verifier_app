package com.owner.dgcverifier.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.owner.dgcverifier.DGCCertificateManager
import com.owner.dgcverifier.DGCCertificateManager.DATE_FORMAT
import com.owner.dgcverifier.network.NetworkObservables
import com.owner.dgcverifier.network.RxBus
import com.owner.dgcverifier.network.SynchResultEvent
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel: ViewModel() {
    private var fetchCertificatesDisposable: Disposable? = null
    var fetchCertificatesResponse= MutableLiveData<Boolean>()

    fun fetchCertificates() {
        fetchCertificatesDisposable = NetworkObservables.getCertificates()
            .subscribe({ response ->
                val newCertificates = response?.map { publicKey ->
                    return@map publicKey.keyValue
                }?.toList() as List<String>
                val updateSucceeded = DGCCertificateManager.add(newCertificates)
                if (updateSucceeded) {
                    val date = Calendar.getInstance().time
                    val formatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
                    val dateString = formatter.format(date)
                    DGCCertificateManager.lastSyncDate = dateString
                }
                fetchCertificatesResponse.value = updateSucceeded
                RxBus.sendEvent(SynchResultEvent(true))
            }, {
                fetchCertificatesResponse.value = false
                RxBus.sendEvent(SynchResultEvent(false))
            })
    }

    override fun onCleared() {
        fetchCertificatesDisposable?.dispose()
        super.onCleared()
    }
}