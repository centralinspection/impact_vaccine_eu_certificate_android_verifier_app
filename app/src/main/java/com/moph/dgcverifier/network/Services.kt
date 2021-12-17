package com.owner.dgcverifier.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface Services {
    @GET("trustList")
    fun getCertificates(): Observable<Response<ResponseBody>>
}