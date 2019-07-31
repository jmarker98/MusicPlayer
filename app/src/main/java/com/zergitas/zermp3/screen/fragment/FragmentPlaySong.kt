package com.zergitas.zermp3.screen.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import com.zergitas.zermp3.R
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.screen.main.MainActivity
import com.zergitas.zermp3.screen.playsong.PlaySongContract
import com.zergitas.zermp3.screen.playsong.PlaySongPresenter
import com.zergitas.zermp3.screen.playsong.interfaces.CallbackSong
import com.zergitas.zermp3.service.ActionSong
import com.zergitas.zermp3.service.SongService
import com.zergitas.zermp3.utils.Contants
import com.zergitas.zermp3.utils.helper.formatTime
import kotlinx.android.synthetic.main.fragment_fragment_play_song.*


class FragmentPlaySong : Fragment(), PlaySongContract.View {

    private val TIME_DELAY: Long = 10
    private var service: SongService? = null
    private var isBound = false
    private var songHandler: Handler? = null
    private var songRunnable: Runnable? = null
    private var onStop = false
    private lateinit var presenter: PlaySongPresenter
    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity
    private var isShuffule: Boolean = false

    override fun getSongs(): ArrayList<Song> {
        return mActivity.intent.getParcelableArrayListExtra(Contants.EXTRA_SONGS)
    }

    override fun getIndexCurrent(): Int {
        return activity!!.intent.getIntExtra(Contants.EXTRA_POSITION, 0)
    }

    override fun isOpenFromNoti(): Boolean {
        return activity!!.intent.getBooleanExtra(Contants.EXTRA_OPEN_FROM_NOTI, false)
    }

    override fun showLoopSetting(isLoop: Boolean) {
        if (isLoop) {
            img_loop.setImageResource(R.drawable.loop)
        } else {
            img_loop.setImageResource(R.drawable.ic_loop_grey)
        }
        service!!.updateSettingLoopSong(isLoop)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragment_play_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = PlaySongPresenter(activity!!.baseContext)
        presenter.setView(this)
        (activity as MainActivity).callingAnimationforThumb()
        tv_name_display.isSelected = true
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        startService()
        super.onResume()
        handlerSong()
        initControlsforSeekBarVolume()
        (activity as MainActivity).callingAnimationforThumb()

    }

    override fun onPause() {
        super.onPause()
        songHandler?.removeCallbacksAndMessages(null)
    }

    override fun onStop() {
        super.onStop()
        this.onStop = true
        val intentStart = Intent(activity?.applicationContext, SongService::class.java)
        intentStart.action = ActionSong.CREAT_NOTI
        activity?.startService(intentStart)
        if (isBound) {

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        mActivity = mContext as MainActivity
    }

    private fun setupView() {
        actiononClick()
    }

    private fun actiononClick() {
        img_play.setOnClickListener(onClick)
        img_next.setOnClickListener(onClick)
        img_previous.setOnClickListener(onClick)
        img_loop.setOnClickListener(onClick)
        img_back.setOnClickListener(onClick)
        img_favourite.setOnClickListener(onClick)
        img_Shuffle.setOnClickListener(onClick)
    }

    private fun startService() {
        val intent = Intent(activity?.applicationContext, SongService::class.java)
        activity!!.startService(intent)
        activity!!.bindService(intent, serviceConnecttion, Context.BIND_AUTO_CREATE)
    }


    private fun setupImageNext() {
        img_next.isEnabled = false
        Handler().postDelayed(object : Runnable {
            override fun run() {
                img_next.isEnabled = true
            }

        }, 1100)

    }

    private fun setupImagePrevious() {
        img_previous.isEnabled = false
        Handler().postDelayed(object : Runnable {
            override fun run() {
                img_previous.isEnabled = true
            }

        }, 1100)

    }

    private fun handlerSong() {
        songHandler = Handler()
        songRunnable = object : Runnable {
            override fun run() {
                try {
                    if (service != null && service!!.getCurrentPosition() != null) {
                        sb_time.progress = service!!.getCurrentPosition()!!
                        tv_current.text = sb_time.progress.toLong().formatTime()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                songHandler?.postDelayed(this, TIME_DELAY)
            }
        }
        songHandler!!.postDelayed(songRunnable, TIME_DELAY)

        sb_time.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                if (fromUser) {
                    service?.seekBarChange(progress)
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    fun initControlsforSeekBarVolume() {
        try {
            var audioManager: AudioManager? = activity!!.getSystemService(AUDIO_SERVICE) as AudioManager

            sb_volume.setMax(audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC))
            sb_volume.setProgress(audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC))


            sb_volume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(arg0: SeekBar) {}

                override fun onStartTrackingTouch(arg0: SeekBar) {}

                override fun onProgressChanged(arg0: SeekBar, progress: Int, arg2: Boolean) {
                    audioManager!!.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        progress, 0
                    )
                    Toast.makeText(activity, "" + ((progress * 100) / 16) + "%", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private val onClick = View.OnClickListener { v ->
        when (v?.id) {
            img_play.id -> {
                service?.changeStatusSong()
            }
            img_next.id -> {
                setupImageNext()
                if (isShuffule == true) {
                    service?.playShuffleSong()
                } else {
                    service?.playNextSong()

                }
            }
            img_previous?.id -> {
                setupImagePrevious()
                if (isShuffule == true) {
                    service?.playShuffleSong()
                } else {
                    service?.playPreviousSong()
                }
            }
            img_loop?.id -> {
                presenter.onUpdateLoopSong()
            }
            img_Shuffle?.id -> {
                if (isShuffule == false) {
                    isShuffule = true
                    Log.d("isShuffle", isShuffule.toString())
                    img_Shuffle.setImageResource(R.drawable.aaaaa)
                }
            }
            img_back?.id -> {
                val fm = activity!!.supportFragmentManager
                fm.popBackStack()
            }
            img_favourite?.id -> {
                (activity as MainActivity).checkLikeSong(service?.getCurrentSong()!!)

            }
        }

    }

    private val callbackSong = object : CallbackSong<Song> {
        override fun updateInfoSong(data: Song) {
            tv_name_display.text = data.name
            tv_singer_display.text = data.singer
            tv_time.text = data.duration.formatTime()
            sb_time.max = data.duration.toInt()
            sb_time.progress = 0
            if (data.liked == true) {
                img_favourite.setImageResource(R.drawable.liked)
            } else {
                img_favourite.setImageResource(R.drawable.like)
            }
        }

        override fun updateStatusSong(status: Boolean) {
            if (status) {
                img_play.setImageResource(R.drawable.ic_pause)
                (activity as MainActivity).callingAnimationforThumb()
            } else {
                img_play.setImageResource(R.drawable.play)
                (activity as MainActivity).stopAnimationforThumb()
            }
        }

        override fun updateThumbSong(thumb: Bitmap?) {
            if (thumb == null) {
                img_rotates.setImageResource(R.drawable.iconmusic)
            } else {
                img_rotates.setImageBitmap(thumb)
            }
        }

    }

    private val serviceConnecttion = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }

        override fun onServiceConnected(name: ComponentName?, services: IBinder?) {
            val binder: SongService.BinderService = services as SongService.BinderService
            service = binder.getService()
            service!!.callback = callbackSong
            presenter.start()
            if (!isOpenFromNoti() && !onStop) {
                service!!.position = getIndexCurrent()
                service!!.songs = getSongs()
                service!!.playSong()
                isBound = true
            } else {
                callbackSong.updateInfoSong(service!!.getCurrentSong()!!)
                callbackSong.updateThumbSong(service!!.thumb)
                callbackSong.updateStatusSong(service!!.isSongPlaying()!!)
            }

        }

    }


}
