package me.rozkmin.mvibinding

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import me.rozkmin.mvibinding.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity(), LoginContract.View {

    val binding: LoginActivityBinding by lazy {
        DataBindingUtil.setContentView<LoginActivityBinding>(this, R.layout.login_activity)
    }

    val presenter: LoginContract.Presenter by lazy {
        LoginPresenter(LoginInteractor())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun emailChangeIntent(): Observable<String> = RxTextView.textChanges(binding.loginEmail)
            .skipInitialValue()
            .map { it.toString() }

    override fun passwordChangeIntent(): Observable<String> = RxTextView.textChanges(binding.loginPassword)
            .skipInitialValue()
            .map { it.toString() }

    override fun submitIntent(): Observable<Pair<String, String>> =
            RxView.clicks(binding.loginSubmit).map { binding.viewState?.let { it.email to it.password } }

    override fun render(viewState: LoginViewState) {
        binding.viewState = viewState
        binding.executePendingBindings()
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}
