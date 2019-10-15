package com.eugeneek.dicontainer

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.eugeneek.dicontainer.di.AppScope
import com.eugeneek.dicontainer.di.CounterScope
import com.eugeneek.dicontainer.di.MainScope
import com.eugeneek.injector.Injector
import com.eugeneek.injector.Injector.get
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var counterIncrementer: CounterIncrementer = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainScope = MainScope(this)
        Injector.openScope(mainScope, parentScopeClass = AppScope::class)

        btnIncrement.setOnClickListener {
            counterIncrementer.apply()
            updateBtn()
            if (counterIncrementer.value >= 10) {
                Injector.closeScope(CounterScope::class)
                Injector.openScope(CounterScope(), AppScope::class)
                counterIncrementer = get()
            }
        }

        updateBtn()

    }

    override fun onDestroy() {
        super.onDestroy()

        Injector.closeScope(MainScope::class)
    }

    @SuppressLint("SetTextI18n")
    private fun updateBtn() {
        tvCounter.text = "Count: ${counterIncrementer.value}\nCreated at: ${counterIncrementer.time}"
    }
}
