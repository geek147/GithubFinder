package com.envios.githubfinder.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.envios.githubfinder.base.BaseViewModel
import com.envios.githubfinder.data.remote.model.GithubUser
import com.envios.githubfinder.data.remote.model.User
import com.envios.githubfinder.data.repository.GithubUserRepository
import com.envios.githubfinder.utils.ErrorUtils
import com.envios.githubfinder.utils.Failed
import com.envios.githubfinder.utils.Loading
import com.envios.githubfinder.utils.ViewModelState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel (private val repository: GithubUserRepository) : BaseViewModel() {

    data class UsersLoaded(val userList: List<User?>? = null) : ViewModelState()


    fun searchUsers(username: String, page: Int, perPage:Int) {
        _states.value = Loading(true)
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val response = repository.searchUsers(username, page, perPage)
                _states.value = UsersLoaded(response.body()?.items)
            } catch (t : Throwable) {
                showErrorMessage(ErrorUtils.getErrorThrowableMsg(t))
            }
        }

    }

}
