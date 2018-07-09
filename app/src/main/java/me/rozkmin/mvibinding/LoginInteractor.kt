package me.rozkmin.mvibinding

import io.reactivex.Observable
import io.reactivex.Observable.just
import java.util.concurrent.TimeUnit

class LoginInteractor : LoginContract.Interactor {
    override fun login(email: String, password: String): Observable<LoginPartialChanges> =
            Observable.timer(2, TimeUnit.SECONDS)
                    .flatMap { if (!email.contains("@")) error(Throwable("wrong email")) else just(it) }
                    .map { LoginPartialChanges.Success }
                    .cast(LoginPartialChanges::class.java)
                    .onErrorReturn { LoginPartialChanges.Error("wrong email") }
                    .startWith(LoginPartialChanges.Loading)
}