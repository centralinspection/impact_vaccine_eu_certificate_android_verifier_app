package com.owner.dgcverifier.network

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

object RxBus {
    private val synchResultBus = BehaviorSubject.create<Event>()
    private val publishEventBus = PublishSubject.create<Event>()


    fun publishEvent(o: Event) {
        publishEventBus.onNext(o)
    }

    val sendEventObservable: Observable<Event>
        get() {
            return synchResultBus
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

    val publishEventObservable: Observable<Event>
        get() {
            return publishEventBus
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

    fun sendEvent(event: Event) {
        when (event) {
            is SynchResultEvent -> synchResultBus.onNext(event)
        }
    }
}