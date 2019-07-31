package com.zergitas.zermp3.screen

interface BasePresenter<T> {
    fun setView(view: T)
    fun start()
    fun stop()
}