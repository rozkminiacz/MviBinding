package me.rozkmin.mvibinding

sealed class LoginPartialChanges {
    abstract fun reduce(previousState: LoginViewState): LoginViewState

    data class EmailChange(private val newValue: String) : LoginPartialChanges() {
        override fun reduce(previousState: LoginViewState): LoginViewState {
            return previousState.copy(
                    email = newValue,
                    submitButtonEnabled = newValue.isNotEmpty() && previousState.password.isNotEmpty())
        }
    }

    data class PasswordChange(private val newValue: String) : LoginPartialChanges() {
        override fun reduce(previousState: LoginViewState): LoginViewState {
            return previousState.copy(
                    password = newValue,
                    submitButtonEnabled = newValue.isNotEmpty() && previousState.email.isNotEmpty())
        }
    }

    object Success : LoginPartialChanges() {
        override fun reduce(previousState: LoginViewState): LoginViewState {
            return previousState.copy(loading = false, showError = false, errorMessage = "")
        }
    }

    object Loading : LoginPartialChanges() {
        override fun reduce(previousState: LoginViewState): LoginViewState {
            return previousState.copy(loading = true)
        }
    }

    data class Error(val message: String) : LoginPartialChanges() {
        override fun reduce(previousState: LoginViewState): LoginViewState {
            return previousState.copy(showError = true, errorMessage = message, loading = false)
        }
    }
}
