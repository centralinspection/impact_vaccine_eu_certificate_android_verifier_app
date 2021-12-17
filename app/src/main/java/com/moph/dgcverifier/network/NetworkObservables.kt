package com.owner.dgcverifier.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.owner.dgcverifier.model.PublicKey

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

object NetworkObservables {
    fun getCertificates(): Observable<List<PublicKey>> {
        return Retrofit.services.getCertificates().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .map { response ->
                val body = response.body()?.string()
                if (body != null) {
                    return@map Gson().fromJson(body, object : TypeToken<List<PublicKey>>() {}.type)
                }
                throw Throwable("Invalid Response")
            }
    }
}