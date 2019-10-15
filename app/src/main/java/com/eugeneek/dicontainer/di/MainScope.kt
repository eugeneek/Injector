package com.eugeneek.dicontainer.di

import android.app.Activity
import com.eugeneek.injector.Scope


class MainScope(
    private val activity: Activity
): Scope() {

    override fun init() {

        bind(activity)
    }
}