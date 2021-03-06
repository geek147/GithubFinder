package com.envios.githubfinder.utils

/**
 * Created by chirikualii on 11/12/18
 * github.com/chirikualii
 */
open class ViewModelState
data class Success(var message: String?="") :ViewModelState()
data class Loading(var isLoading: Boolean) : ViewModelState()
data class Failed(var error: String?="") : ViewModelState()