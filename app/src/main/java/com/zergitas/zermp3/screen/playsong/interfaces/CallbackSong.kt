package com.zergitas.zermp3.screen.playsong.interfaces

import android.graphics.Bitmap

interface CallbackSong<T> {
    fun updateInfoSong(data:T)

    fun updateStatusSong(status:Boolean)

    fun updateThumbSong(thumb: Bitmap?)
}