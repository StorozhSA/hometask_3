package ru.skillbranch.skillarticles.viewmodels.auth

import androidx.lifecycle.SavedStateHandle
import ru.skillbranch.skillarticles.data.repositories.RootRepository
import ru.skillbranch.skillarticles.extensions.isValidEmail
import ru.skillbranch.skillarticles.viewmodels.base.BaseViewModel
import ru.skillbranch.skillarticles.viewmodels.base.IViewModelState
import ru.skillbranch.skillarticles.viewmodels.base.NavigationCommand
import ru.skillbranch.skillarticles.viewmodels.base.Notify

class AuthViewModel(handle: SavedStateHandle) : BaseViewModel<AuthState>(handle, AuthState()) {
    private val repository = RootRepository

    init {
        subscribeOnDataSource(repository.isAuth()) { isAuth, state ->
            state.copy(isAuth = isAuth)
        }
    }

    fun handleLogin(login: String, pass: String, dest: Int?) {
        launchSafety {
            repository.login(login, pass)
            navigate(NavigationCommand.FinishLogin(dest))
        }
    }

    fun handleRegister(name: String, login: String, password: String, dest: Int?) {
        if (isValid(name, login, password)) {
            launchSafety {
                repository.handleRegister(name, login, password, dest)
                navigate(NavigationCommand.FinishLogin(dest))
            }
        }
    }

    private fun isValid(name: String, login: String, password: String): Boolean {
        var isValid = false
        if (name.isEmpty() || login.isEmpty() || password.isEmpty()) {
            notify(Notify.ErrorMessage("Name, login, password it is required fields and not must be empty"))
        } else if (name.length < 3 || !Regex("^[a-zA-Z0-9_-]*\$").matches(name)) {
            notify(Notify.ErrorMessage("The name must be at least 3 characters long and contain only letters and numbers and can also contain the characters \"-\" and \"_\""))
        } else if (!login.isValidEmail()) {
            notify(Notify.ErrorMessage("Incorrect Email entered"))
        } else if (password.length < 8 || !Regex("^[a-zA-Z0-9]*\$").matches(password)) {
            notify(Notify.ErrorMessage("Password must be at least 8 characters long and contain only letters and numbers"))
        } else if (password != currentState.repeatPass) {
            notify(Notify.ErrorMessage("Password does not match"))
        } else {
            isValid = true
        }
        return isValid
    }

    fun handreRepeatPass(password: String) {
        updateState { it.copy(repeatPass = password) }
    }


}

data class AuthState(
    val isAuth: Boolean = false,
    val repeatPass: String = ""
) : IViewModelState