package com.envios.githubfinder.base

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.envios.githubfinder.utils.Failed
import com.envios.githubfinder.utils.Loading
import com.envios.githubfinder.utils.Success
import com.envios.githubfinder.utils.ViewModelState

abstract class BaseViewModel : ViewModel() {

    val _states = MutableLiveData<ViewModelState>()
    private val loading = Loading(false)
    private var failed = Failed()
    private var success = Success()
    val states: LiveData<ViewModelState>
        get() = _states


    @CallSuper
    override fun onCleared() {
        super.onCleared()
    }

    fun showLoading(isLoading: Boolean){
        loading.isLoading = isLoading
        _states.value = loading
    }

    fun showErrorMessage(message: String?=null){
        if(message!= null){
            failed.error = message
            _states.value = failed
        }
    }

    fun showSuccessMessage(message:String?=null){
        success.message = message
        _states.value = success
    }
}