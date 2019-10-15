package com.eugeneek.dicontainer

import android.app.Application
import com.eugeneek.dicontainer.di.AppScope
import com.eugeneek.dicontainer.di.CounterScope
import com.eugeneek.injector.Injector


class App: Application() {

    override fun onCreate() {
        super.onCreate()

        val appScope = AppScope(this)
        val counterScope = CounterScope()

        Injector.openScope(appScope)
        Injector.openScope(counterScope, parentScopeClass = AppScope::class)
    }
}