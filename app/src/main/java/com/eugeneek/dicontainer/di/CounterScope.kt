package com.eugeneek.dicontainer.di

import com.eugeneek.dicontainer.Counter
import com.eugeneek.dicontainer.CounterIncrementer
import com.eugeneek.injector.Scope


class CounterScope: Scope() {

    override fun init() {

        val counter = Counter(0)
        val counterIncrementer = CounterIncrementer(
            counter = counter
        )

        bind(counterIncrementer)
    }
}