package com.envios.githubfinder

import android.app.Application
import com.envios.githubfinder.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GithubFinderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@GithubFinderApp)
            modules(listOf(appModules))
        }
    }
}