package com.envios.githubfinder.ui.main

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.envios.githubfinder.data.remote.model.User

class GithubUserListViewModel(user: User?): ViewModel() {

    var name: ObservableField<String> = ObservableField()
    var imageUrl : ObservableField<String> = ObservableField()

    init {
        name.set(user?.login)

        imageUrl.set(user?.avatarUrl)


    }
}