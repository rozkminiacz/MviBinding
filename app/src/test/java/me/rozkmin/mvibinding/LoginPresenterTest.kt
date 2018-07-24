package me.rozkmin.mvibinding

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable.just
import me.rozkmin.mvibinding.LoginPartialChanges.Error
import me.rozkmin.mvibinding.LoginPartialChanges.Loading
import me.rozkmin.mvibinding.LoginPartialChanges.Success
import org.junit.Test

/**
 * Created by jaroslawmichalik on 24.07.2018
 */
class LoginPresenterTest {
    @Test
    fun `given wrong credentials when user submit login then error state is rendered`() {
        val navigator: Navigator = mock()
        val interactor: LoginContract.Interactor = mock {
            on { login("asd@dsa.com", "Testowe1") } doReturn just(Loading, Error("wrong credentials"))
            on { login("asd@dsa.com", "Testowe1@") } doReturn just(Loading, Success)
        }
        val loginViewRobot = LoginViewRobot(LoginPresenter(interactor, navigator))

        with(loginViewRobot) {
            provideEmail("asd@dsa.com")
            providePassword("Testowe1")
            submitLogin("asd@dsa.com", "Testowe1")
        }


        loginViewRobot.assertViewStatesRendered(
                LoginViewState(),
                LoginViewState(email = "asd@dsa.com"),
                LoginViewState(email = "asd@dsa.com", password = "Testowe1", submitButtonEnabled = true),
                LoginViewState(email = "asd@dsa.com", password = "Testowe1", submitButtonEnabled = true, loading = true),
                LoginViewState(email = "asd@dsa.com", password = "Testowe1", submitButtonEnabled = true, loading = false, showError = true, errorMessage = "wrong credentials")
        )

    }
}