package me.rozkmin.mvibinding

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable.just
import junit.framework.Assert.assertEquals
import me.rozkmin.mvibinding.LoginPartialChanges.Error
import me.rozkmin.mvibinding.LoginPartialChanges.Loading
import me.rozkmin.mvibinding.LoginPartialChanges.Success
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Created by jaroslawmichalik on 24.07.2018
 */
class LoginPresenterSpecification : Spek({
    describe("login presenter") {

        val navigator: Navigator = mock()

        val interactor: LoginContract.Interactor = mock {
            on { login("asd@dsa.com", "Testowe1") } doReturn just(Loading, Error("wrong credentials"))
            on { login("asd@dsa.com", "Testowe1@") } doReturn just(Loading, Success)
        }
        val loginViewRobot = LoginViewRobot(LoginPresenter(interactor, navigator))

        on("submit wrong credentials") {
            with(loginViewRobot) {
                provideEmail("asd@dsa.com")
                providePassword("Testowe1")
                submitLogin("asd@dsa.com", "Testowe1")
            }

            it("should render error state") {
                loginViewRobot.checkViewStates {
                    assertEquals(LoginViewState(
                            email = "asd@dsa.com",
                            password = "Testowe1",
                            loading = false,
                            submitButtonEnabled = true,
                            errorMessage = "wrong credentials",
                            showError = true
                    ), it.last())
                }
            }
        }

        on("submit valid credentials") {
            with(loginViewRobot) {
                providePassword("Testowe1@")
                submitLogin("asd@dsa.com", "Testowe1@")
            }
            it("should render success") {
                loginViewRobot.checkViewStates {
                    loginViewRobot.checkViewStates {
                        assertEquals(LoginViewState(
                                email = "asd@dsa.com",
                                password = "Testowe1@",
                                loading = false,
                                submitButtonEnabled = true,
                                errorMessage = "",
                                showError = false
                        ), it.last())
                    }
                }
            }
            it("should navigate to main screen") {
                verify(navigator).openMainScreen()
            }
        }
    }
})