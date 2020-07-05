package com.envios.githubfinder.di

import androidx.room.Room
import com.envios.githubfinder.data.remote.GithubUserFactory
import com.envios.githubfinder.data.repository.GithubUserRepository
import com.envios.githubfinder.ui.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.android.ext.koin.androidApplication



val appModules = module {
    single { GithubUserFactory.create() }
    single { GithubUserFactory.getHttpLoggingInterceptor() }
    single { GithubUserFactory.getOkHttpClient(get()) }

    single { GithubUserRepository(get())}

    viewModel { MainViewModel(get()) }
}

