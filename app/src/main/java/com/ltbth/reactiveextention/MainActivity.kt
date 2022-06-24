package com.ltbth.reactiveextention

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val observable = getObservableUser() // Phat ra du lieu dua tren ham onNext
        val observer = getObserverUser() // Lang nghe
        observable.subscribeOn(Schedulers.io()) // Phat ra du lieu
            .observeOn(AndroidSchedulers.mainThread()) // Lang nghe du lieu hien thi len UI
            .subscribe(observer) // Lang nghe
    }

    private fun getObserverUser(): Observer<User> {
        return object: Observer<User> {
            override fun onSubscribe(d: Disposable) {
                Log.e("test","onSubscribe")
                disposable = d
            }

            override fun onNext(t: User) {
                Log.e("test","onNext $t")
            }

            override fun onError(e: Throwable) {
                Log.e("test","onError")
            }

            override fun onComplete() {
                Log.e("test","onComplete")
            }
        }
    }

    private fun getObservableUser(): Observable<User> {
        val users = getUsers()
        return Observable.create {
            try {
                for (user in users) {
                    // Chua bi huy ket noi
                    if (!it.isDisposed) {
                        it.onNext(user)
                    }
                }
                if (!it.isDisposed) {
                    it.onComplete()
                }
            } catch (ex: Exception) {
                it.onError(ex)
            }
        }
    }

    private fun getUsers(): List<User> {
        val users = arrayListOf<User>()
        users.add(User(1,"trung 1"))
        users.add(User(2,"trung 2"))
        users.add(User(3,"trung 3"))
        users.add(User(4,"trung 4"))
        users.add(User(5,"trung 5"))
        return users
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}