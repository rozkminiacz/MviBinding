package me.rozkmin.mvibinding

/**
 * Created by jaroslawmichalik on 06.07.2018
 */
data class LoginViewState(
        val email: String = "",
        val password: String = "",
        val submitButtonEnabled: Boolean = false,
        val loading: Boolean = false,
        val showError: Boolean = false,
        val errorMessage: String = "")