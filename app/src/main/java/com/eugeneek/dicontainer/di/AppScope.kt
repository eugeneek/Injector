package com.eugeneek.dicontainer.di

import android.content.Context
import com.eugeneek.injector.Scope


class AppScope(
    private val context: Context
): Scope() {

    override fun init() {
        bind(context)
    }
}