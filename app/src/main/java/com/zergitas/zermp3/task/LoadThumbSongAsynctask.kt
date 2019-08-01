package com.zergitas.zermp3.task

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import com.zergitas.zermp3.data.source.local.song.provider.ContentProvider
import com.zergitas.zermp3.screen.playsong.interfaces.OnLoadThumb

class LoadThumbSongAsynctask(
    private var context: Context,
    private var onLoadThumb: OnLoadThumb
) : AsyncTask<Long, Void, Bitmap>() {
    override fun doInBackground(vararg params: Long?): Bitmap? {
        try {
            return params!![0]?.let { ContentProvider.getThumbSong(context, it) }
        } catch (e: Exception) {
            return null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        if (result != null) {
            onLoadThumb.onSuccess(result!!)
        } else {
            onLoadThumb.onFail()
        }
    }

}