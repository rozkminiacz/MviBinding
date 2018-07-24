package me.rozkmin.mvibinding

import io.reactivex.Observable
import io.reactivex.Observable.mergeArray
import io.reactivex.disposables.CompositeDisposable

class LoginPresenter(
        private val interactor: LoginContract.Interactor,
        private val navigator: Navigator) : LoginContract.Presenter() {

    private val compositeDisposable = CompositeDisposable()

    override fun bindIntents() {

        val emailEditIntent = intent { it.emailChangeIntent() }
                .map { LoginPartialChanges.EmailChange(it) }

        val passwordEditIntent = intent { it.passwordChangeIntent() }
                .map { LoginPartialChanges.PasswordChange(it) }

        val submitIntent = intent { it.submitIntent() }
                .flatMap { interactor.login(it.first, it.second) }
                .doOnNext {
                    if (it == LoginPartialChanges.Success) navigator.openMainScreen()
                }

        val forgotPasswordIntent = intent { it.forgotPasswordIntent() }
                .doOnNext { navigator.openForgotPasswordScreen() }

        compositeDisposable.add(forgotPasswordIntent.subscribe())

        val allIntentsObservable: Observable<LoginPartialChanges> = mergeArray(
                emailEditIntent,
                passwordEditIntent,
                submitIntent)

        subscribeViewState(allIntentsObservable.scan(LoginViewState(), this::viewStateReducer), LoginContract.View::render)
    }

    override fun unbindIntents() {
        compositeDisposable.dispose()
        super.unbindIntents()
    }

    private fun viewStateReducer(previousState: LoginViewState, partialChanges: LoginPartialChanges) = partialChanges.reduce(previousState)
}