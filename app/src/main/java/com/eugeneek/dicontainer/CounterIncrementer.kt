package com.eugeneek.dicontainer


class CounterIncrementer(
    private val counter: Counter
) {

    val time = System.currentTimeMillis()

    val value: Int
        get() = counter.count

    fun apply () {
        counter.increment()
    }
}