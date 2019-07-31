package com.zergitas.zermp3.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.*
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.RemoteViews
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.screen.fragment.FragmentPlaySong
import com.zergitas.zermp3.screen.main.MainActivity
import com.zergitas.zermp3.screen.playsong.interfaces.CallbackSong
import com.zergitas.zermp3.screen.playsong.interfaces.OnLoadThumb
import com.zergitas.zermp3.service.ActionSong.Companion.CREAT_NOTI
import com.zergitas.zermp3.service.ActionSong.Companion.NEXT
import com.zergitas.zermp3.service.ActionSong.Companion.PLAY
import com.zergitas.zermp3.service.ActionSong.Companion.PRE
import com.zergitas.zermp3.task.LoadThumbSongAsynctask
import com.zergitas.zermp3.utils.Contants
import kotlinx.android.synthetic.main.fragment_fragment_play_song.*
import java.lang.Process
import java.util.*
import android.media.ToneGenerator.MAX_VOLUME



class SongService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private val ID = 1000
    var position: Int = 0
    var songs: ArrayList<Song>?=null
    var callback: CallbackSong<Song>? = null
    var thumb: Bitmap? = null

    private var iBinder = BinderService()
    private var mediaPlayer: MediaPlayer? = null
    private var isLoop: Boolean = false
    private var changeSong: Boolean = false


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) receiveIntent(intent)
        return START_STICKY

    }

    override fun onBind(intent: Intent?): IBinder? {
        return iBinder
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
        Handler().postDelayed(object : Runnable {
            override fun run() {
                changeSong = true
                updateNotification(songs!![position])
                callback?.updateStatusSong(mp!!.isPlaying)
            }

        }, 100)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playNextSong()
        updateNotification(songs!![position])
    }

    fun creatNotification(song: Song): Notification {
        val remoteViews = creatRemoteViews(song)
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        i.putExtra(Contants.EXTRA_OPEN_FROM_NOTI, true)
        val uniqueInt = (System.currentTimeMillis() and 0xfffffff).toInt()
        val intent = PendingIntent.getActivity(
            this, uniqueInt, i,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this)
            .setSmallIcon(com.zergitas.zermp3.R.drawable.icon)
            .setContentIntent(intent)
        val notification = builder.build()
        notification.contentView = remoteViews
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification.bigContentView = remoteViews
        }
        return notification
    }

    fun updateNotification(song: Song) {
        val remoteViews = creatRemoteViews(song)
        if (mediaPlayer!!.isPlaying) {
            remoteViews.setImageViewResource(
                com.zergitas.zermp3.R.id.image_play,
                com.zergitas.zermp3.R.drawable.ic_pause
            )
        } else {
            remoteViews.setImageViewResource(
                com.zergitas.zermp3.R.id.image_play,
                com.zergitas.zermp3.R.drawable.ic_play
            )
        }
        if (thumb != null) remoteViews.setImageViewBitmap(com.zergitas.zermp3.R.id.image_thumb, thumb)
        else remoteViews.setImageViewResource(
            com.zergitas.zermp3.R.id.image_thumb,
            com.zergitas.zermp3.R.drawable.icon
        )

        val i = Intent(this, FragmentPlaySong::class.java)
        i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val intent = PendingIntent.getActivity(
            this, 0, i,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        i.putExtra(Contants.EXTRA_OPEN_FROM_NOTI, true)
        i.putExtra(Contants.EXTRA_POSITION, position)
        val builder = NotificationCompat.Builder(this)
            .setSmallIcon(com.zergitas.zermp3.R.mipmap.ic_launcher)
            .setContentIntent(intent)

        val notification = builder.build()
        notification.contentView = remoteViews
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification.bigContentView = remoteViews
        }
        NotificationManagerCompat.from(this).notify(ID, notification)
    }


    private fun creatRemoteViews(song: Song): RemoteViews {
        val remoteViews = RemoteViews(packageName, com.zergitas.zermp3.R.layout.notification)
        remoteViews.setTextViewText(com.zergitas.zermp3.R.id.tv_song, song.name)
        remoteViews.setTextViewText(com.zergitas.zermp3.R.id.tv_singer, song.singer)
        val pendingNext = creatPendingIntent(NEXT)
        val pendingPre = creatPendingIntent(PRE)
        val pendingPlay = creatPendingIntent(PLAY)
        remoteViews.setOnClickPendingIntent(com.zergitas.zermp3.R.id.image_prev, pendingPre)
        remoteViews.setOnClickPendingIntent(com.zergitas.zermp3.R.id.image_next, pendingNext)
        remoteViews.setOnClickPendingIntent(com.zergitas.zermp3.R.id.image_play, pendingPlay)
        return remoteViews
    }

    private fun creatPendingIntent(@ActionSong action: String): PendingIntent {
        val intent = Intent(this, SongService::class.java)
        intent.action = action
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun receiveIntent(intent: Intent) {
        when (intent.action) {
            CREAT_NOTI -> startForegroundService()
            PLAY -> {
                changeStatusSong()
                updateNotification(songs!![position])
            }
            NEXT -> {
                playNextSong()
            }
            PRE -> {
                playPreviousSong()
            }
        }
    }

    /**
     * start foreground service
     */
    private fun startForegroundService() {
        if (songs != null) {
            startForeground(ID, creatNotification(songs!![position]))
        }
    }

    fun getCurrentPosition() = mediaPlayer?.currentPosition

    fun getCurrentSong() = songs?.get(position)

    fun isSongPlaying() = mediaPlayer?.isPlaying

    /**
     * load Song
     */
    fun playSong() {
        LoadThumbSongAsynctask(baseContext, onLoadThumb).execute(songs!![position].idAlbum)
        callback?.updateInfoSong(songs!![position])
        try {
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                stopSong()
            }
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(songs!![position].path)
            mediaPlayer?.setOnPreparedListener(this)
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnCompletionListener(this)
            mediaPlayer?.isLooping = isLoop
        } catch (e: Exception) {
            //load song error
        }
    }

    /**
     * stop and release
     */
    private fun stopSong() {
        mediaPlayer?.stop()
//        mediaPlayer?.release()
    }

    /**
     * play or pause song
     */
    fun changeStatusSong() {
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
        }
        //update status song for UI
        callback?.updateStatusSong(mediaPlayer!!.isPlaying)
    }

    /**
     * play next song
     */
    fun playNextSong() {
        position++
        if (position == songs!!.size) {
            position = 0
        }
        stopSong()
        playSong()

    }

    /**
     * play previous song
     */
    fun playPreviousSong() {
        position--
        if (position == -1) {
            position = 0
        }
        stopSong()
        playSong()
    }


    fun playShuffleSong() {
        Collections.shuffle(songs)
        stopSong()
        playSong()
    }

    /**
     * setting loop mediaplayer
     */
    fun updateSettingLoopSong(isLoop: Boolean) {
        this.isLoop = isLoop
        if (mediaPlayer != null) {
            mediaPlayer?.isLooping = isLoop
        }
    }

    fun seekBarChange(progress: Int) {
        mediaPlayer?.seekTo(progress)
    }

    private val onLoadThumb = object : OnLoadThumb {
        override fun onFail() {
            this@SongService.thumb = null
            updateNotification(songs!![position])
            callback?.updateThumbSong(null)
        }

        override fun onSuccess(bitmap: Bitmap) {
            this@SongService.thumb = bitmap
            updateNotification(songs!![position])
            callback?.updateThumbSong(bitmap)
        }
    }

    inner class BinderService : Binder() {

        fun getService(): SongService = this@SongService

    }


}