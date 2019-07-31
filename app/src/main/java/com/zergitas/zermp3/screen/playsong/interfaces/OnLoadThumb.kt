package com.zergitas.zermp3.screen.playsong.interfaces

import android.graphics.Bitmap

interface OnLoadThumb {
    fun onSuccess(bitmap: Bitmap)
    fun onFail()
}