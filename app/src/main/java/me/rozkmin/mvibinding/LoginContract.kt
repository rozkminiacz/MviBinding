package me.rozkmin.mvibinding

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

/**
 * Created by jaroslawmichalik on 06.07.2018
 */
interface LoginContract {
    interface View : MvpView {
        fun emailChangeIntent(): Observable<String>
        fun passwordChangeIntent(): Observable<String>
        fun submitIntent(): Observable<Pair<String, String>>
        fun render(viewState: LoginViewState)
    }

    abstract class Presenter : MviBasePresenter<View, LoginViewState>()

    interface Interactor {
        fun login(email: String, password: String): Observable<LoginPartialChanges>
    }
}