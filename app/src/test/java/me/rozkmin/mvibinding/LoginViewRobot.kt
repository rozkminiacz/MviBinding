package me.rozkmin.mvibinding

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertArrayEquals

/**
 * Created by jaroslawmichalik on 24.07.2018
 */
class LoginViewRobot(val presenter: LoginPresenter) {

    private val renderedStates = mutableListOf<LoginViewState>()

    private val emailChangeSubject = PublishSubject.create<String>()
    private val passwordChangeSubject = PublishSubject.create<String>()
    private val submitSubject = PublishSubject.create<Pair<String, String>>()
    private val forgotSubject = PublishSubject.create<Any>()

    private val view: LoginContract.View = object : LoginContract.View {
        override fun emailChangeIntent(): Observable<String> = emailChangeSubject

        override fun passwordChangeIntent(): Observable<String> = passwordChangeSubject

        override fun submitIntent(): Observable<Pair<String, String>> = submitSubject

        override fun forgotPasswordIntent(): Observable<Any> = forgotSubject

        override fun render(viewState: LoginViewState) {
            renderedStates.add(viewState)
        }
    }

    init {
        presenter.attachView(view)
    }

    fun checkViewStates(checkFunction: (List<LoginViewState>) -> Unit) {
        checkFunction(renderedStates)
    }

    fun assertViewStatesRendered(vararg expectedViewStates: LoginViewState) {
        assertArrayEquals(expectedViewStates, renderedStates.toTypedArray())
    }

    fun submitLogin(login: String, password: String) {
        submitSubject.onNext(login to password)
    }

    fun provideEmail(email: String) {
        emailChangeSubject.onNext(email)
    }

    fun providePassword(password: String) {
        passwordChangeSubject.onNext(password)
    }
}