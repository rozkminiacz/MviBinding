package me.rozkmin.mvibinding

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import me.rozkmin.mvibinding.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity(), LoginContract.View, Navigator {

    val binding: LoginActivityBinding by lazy {
        DataBindingUtil.setContentView<LoginActivityBinding>(this, R.layout.login_activity)
    }

    val presenter: LoginContract.Presenter by lazy {
        LoginPresenter(LoginInteractor(), this)
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

    override fun forgotPasswordIntent(): Observable<Any> = RxView.clicks(binding.loginForgotPassword)

    override fun render(viewState: LoginViewState) {
        binding.viewState = viewState
        binding.executePendingBindings()
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    override fun openForgotPasswordScreen() {
        Toast.makeText(this, "Open forgot password", Toast.LENGTH_SHORT).show()
    }

    override fun openMainScreen() {
        Toast.makeText(this, "Open main screen", Toast.LENGTH_SHORT).show()
    }
}
