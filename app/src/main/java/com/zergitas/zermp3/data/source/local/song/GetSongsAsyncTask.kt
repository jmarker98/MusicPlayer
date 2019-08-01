package com.zergitas.zermp3.data.source.local.song

import android.content.Context
import android.os.AsyncTask
import com.zergitas.zermp3.R
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.data.source.SongDataSource
import com.zergitas.zermp3.utils.helper.scaleAndCropCenter

class GetSongsAsyncTask(private var callback: SongDataSource.Callback<Song>) :
    AsyncTask<Context, Pair<List<Song>?, Pair<Int, Song>?>, List<Song>>() {
    override fun doInBackground(vararg params: Context?): List<Song> {
        var songs: List<Song> = com.zergitas.zermp3.data.source.local.song.provider.ContentProvider.getSongs(params[0]!!)
        publishProgress(Pair(songs, null))
        getThumbSongs(params[0]!!, songs)
        return songs
    }
    override fun onProgressUpdate(vararg values: Pair<List<Song>?, Pair<Int, Song>?>?) {
        val v = values[0]
        if (v!!.first != null) {
            callback.onGetDataSuccess(v.first!!)
        } else {
            callback.onUpdateData(v!!.second!!)
        }
    }

    override fun onPostExecute(result: List<Song>?) {
        this.cancel(true)
    }

    private fun getThumbSongs(context: Context, songs: List<Song>) {
        val size :Int=context.resources.getDimensionPixelSize(R.dimen._50sdp)
        for ((index, song) in songs.withIndex()) {
            try {
                val thumb = com.zergitas.zermp3.data.source.local.song.provider.ContentProvider.getThumbSong(context, song.idAlbum)
                songs[index].thumb = thumb.scaleAndCropCenter(size, size)
                publishProgress(Pair(null, Pair(index, songs[index])))
            } catch (e: Exception) {
            }
        }
    }
}
