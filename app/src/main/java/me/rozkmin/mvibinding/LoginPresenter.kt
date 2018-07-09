package me.rozkmin.mvibinding

import io.reactivex.Observable
import io.reactivex.Observable.mergeArray
import io.reactivex.android.schedulers.AndroidSchedulers

class LoginPresenter(private val interactor: LoginContract.Interactor) : LoginContract.Presenter() {
    override fun bindIntents() {

        val emailEditIntent = intent { it.emailChangeIntent() }
                .map { LoginPartialChanges.EmailChange(it) }

        val passwordEditIntent = intent { it.passwordChangeIntent() }
                .map { LoginPartialChanges.PasswordChange(it) }

        val submitIntent = intent { it.submitIntent() }
                .flatMap { interactor.login(it.first, it.second) }
                .observeOn(AndroidSchedulers.mainThread())

        val allIntentsObservable: Observable<LoginPartialChanges> = mergeArray(
                emailEditIntent,
                passwordEditIntent,
                submitIntent)

        subscribeViewState(allIntentsObservable.scan(LoginViewState(), this::viewStateReducer), LoginContract.View::render)
    }

    private fun viewStateReducer(previousState: LoginViewState, partialChanges: LoginPartialChanges) = partialChanges.reduce(previousState)
}