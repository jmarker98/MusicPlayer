package com.zergitas.zermp3.screen.main

import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.zergitas.zermp3.data.model.Album
import com.zergitas.zermp3.data.model.Playlist
import com.zergitas.zermp3.data.model.Singer
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.data.source.local.song.db.DatabaseHelper
import com.zergitas.zermp3.screen.fragment.FragmentEqualizer
import com.zergitas.zermp3.screen.fragment.FragmentFavouriteSongs
import com.zergitas.zermp3.screen.fragment.FragmentLibrary
import com.zergitas.zermp3.screen.fragment.FragmentSettings
import com.zergitas.zermp3.screen.main.contract.MainContract
import com.zergitas.zermp3.screen.main.presenter.MainPresenter
import com.zergitas.zermp3.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottomsheetplay.*
import kotlinx.android.synthetic.main.fragment_fragment_play_song.*


class MainActivity : AppCompatActivity(), MainContract.View {

    var tempPlaylistId: Int = -1
    var temPlaylistName: String = ""
    var temNumberSong: Int = 0
    var temSingername = ""
    var temNumberSongofSinger = 0
    var temAlbumName = ""
    var temNumberSongofALbum = 0
    var temNewNameforPlaylist = ""

    var database: DatabaseHelper? = null
    lateinit var sqlite: SQLiteDatabase
    val manager = supportFragmentManager
    val REQUEST_CODE_PERMISSION = 100
    var songs: ArrayList<Song>? = null
    var favouriteList: ArrayList<Song>? = null
    var listSongById: ArrayList<Song>? = null
    var listIdSongfromDb: ArrayList<Long>? = null
    var listPlaylist: ArrayList<Playlist>? = null
    var listSinger: ArrayList<String>? = null
    var listSingerfromDb: ArrayList<Singer>? = null
    var listSongBySinger: ArrayList<Song>? = null
    var listIdAlbum: ArrayList<Long>? = null
    var listAlbum: ArrayList<Album>? = null
    var listSongByIdAlbum: ArrayList<Song>? = null

    private lateinit var presenter: MainPresenter

    val PERMISSION = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun showProgressBar() {
    }

    override fun hiddenProgressBar() {
    }

    override fun addSongs(songs: List<Song>) {
        if (database?.checkCountofTbl_Song() == 0) {
            for (song in songs) {
                Log.d("album", song.album)
                database?.insertData(
                    song.id,
                    song.idAlbum,
                    song.name,
                    song.singer,
                    song.path,
                    song.duration,
                    song.date,
                    song.size,
                    song.album,
                    song.liked
                )
            }

        } else {
            getSingerandAddtoDb()
            getAllalbumandInserttoDb()
            Log.d("failed", "failed")
        }
        sqlite = database!!.getWritableDatabase()
        this.songs = database!!.getData()
    }

    override fun updateSongs(pair: Pair<Int, Song>) {
    }

    override fun showErrorSongs() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.zergitas.zermp3.R.layout.activity_main)
        presenter = MainPresenter(baseContext)
        presenter.setView(this)
        database = DatabaseHelper(this)

        getFavouriteList()
        checkPermision()
        onClickNavigationItem()
        supportFragmentManager.beginTransaction().replace(com.zergitas.zermp3.R.id.frame_View, FragmentLibrary())
            .commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish()
            } else {
                presenter.getSongs()
            }
        }
    }

    fun checkLikeSong(song: Song) {
        if (song.liked == false) {
            song.liked = true
            database?.updateData(song.id, true)
            img_favourite.setImageResource(com.zergitas.zermp3.R.drawable.liked)
            Log.e("aa", song.liked.toString())
            Log.e("ID", song.id.toString())
            Toast.makeText(this, "Added Favourite List", Toast.LENGTH_SHORT).show()
            favouriteList!!.add(song)
        } else {
            song.liked = false
            database?.updateData(song.id, false)
            img_favourite.setImageResource(com.zergitas.zermp3.R.drawable.like)
            Log.e("ID", song.id.toString())
            Log.e("aa", song.liked.toString())
            Toast.makeText(this, "Deleted Favourite List", Toast.LENGTH_SHORT).show()
            favouriteList!!.remove(song)
        }
    }

    private fun checkPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val p: Array<String>? = Utils.checkPermission(baseContext, PERMISSION)
            if (p != null) {
                requestPermissions(p, REQUEST_CODE_PERMISSION)
            } else presenter.getSongs()
        } else presenter.getSongs()
    }

    fun getFavouriteList() {
        favouriteList = ArrayList()
        favouriteList = database?.getFavouriteSongs()
    }

    fun insertPlaylist(id_playlist: Int, name: String) {
        database?.inset_playlist(id_playlist, name)
    }

    fun insertIdsong(id_song: Long, id_playlist: Int) {
        database?.inset_playlist_song(id_song, id_playlist)
    }

    fun getIdSong(id: Int): ArrayList<Long> {
        listIdSongfromDb = database!!.getIdsong(id)
        return listIdSongfromDb!!
    }

    fun getAllPlaylist(): ArrayList<Playlist>? {
        listPlaylist = ArrayList()
        listPlaylist = database?.getAllPlaylist()

        return listPlaylist
    }

    fun getSongById() {
        listSongById = ArrayList()
        var song: Song
        for (i in listIdSongfromDb!!) {
            song = database?.getSongById(i)!!
            listSongById!!.add(song)
        }

        for (j in listSongById!!) {
            Log.d("checksong", j.name)
        }
    }

    fun getcountSongs(id_playlist: Int): Int {
        var count: Int = database?.countSongs(id_playlist)!!
        return count
    }

    fun deletePlaylistById(id_playlist: Int) {
        database?.deletePlaylist(id_playlist)
    }

    fun deleteAllPlaylist() {
        database?.deleteAllPlaylist()
    }

    fun editPlaylistName(id_playlist: Int) {
        database?.updatePlaylist(temNewNameforPlaylist, id_playlist)
    }

    fun getSingerandAddtoDb() {
        if (database!!.checkCountofTbl_singer() == 0) {
            Log.d("checksinger", database!!.checkCountofTbl_singer().toString())
            listSinger = ArrayList()
            listSinger = database?.getAllSinger()
            for (i: String in listSinger!!) {
                database?.inserSingertoDb(i)
                Log.d("singer", i)
            }
        } else {
            Log.d("checksinger", "ok")
        }
        //database?.deleteAllSinger()
    }

    fun getSingerfromDb(): ArrayList<Singer>? {
        listSingerfromDb = ArrayList()
        listSingerfromDb = database?.getSingerfromDb()

        return listSingerfromDb

    }

    fun getSongBySinger(singer: String) {
        listSongBySinger = ArrayList()
        listSongBySinger = database?.getSongBySinger(singer)

        temNumberSongofSinger = listSongBySinger!!.size

        Log.d("sizeaa", temNumberSongofSinger.toString())
        for (k: Song in listSongBySinger!!) {
            Log.d("songbySinger", k.name)
        }
    }

    fun sortSongsByName() {
        songs!!.sortBy {
            it.name
        }
    }

    fun sortSongsBySinger() {
        songs!!.sortBy {
            it.singer
        }
    }

    fun getAllalbumandInserttoDb() {
        listIdAlbum = ArrayList()
        listIdAlbum = database?.getIdAlumfromtbl_song()

        for (i: Long in listIdAlbum!!) {
            Log.d("albumId", i.toString())
            Log.d("albumName", database?.getAlbumfromIdAlbum(i))

            database?.insertAlbum(i, database?.getAlbumfromIdAlbum(i)!!)
        }
    }

    fun getAlbumfromDb(): ArrayList<Album>? {
        listAlbum = ArrayList()
        listAlbum = database?.getListAlbumfrom_tbl_album()!!

        Log.d("albumsize", listAlbum!!.size.toString())
        return listAlbum
    }

    fun getSongByIdAlbum(id_album: Long) {
        listSongByIdAlbum = ArrayList()
        listSongByIdAlbum = database?.getSongByIdAlbums(id_album)

        temNumberSongofALbum = listSongByIdAlbum!!.size
    }

    fun callingAnimationforThumb() {
        var animation: Animation = AnimationUtils.loadAnimation(this, com.zergitas.zermp3.R.anim.rotate)
        img_rotates.startAnimation(animation)
    }

    fun stopAnimationforThumb() {
        img_rotates.clearAnimation()
    }

    fun customBottomSheet() {
        tv_name_display_bottomsheet.isSelected = true
        bottomSheetPlayingsong.visibility = View.VISIBLE
        val behavior = BottomSheetBehavior.from(bottomSheetPlayingsong)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
                tv_singer_display_bottomsheet.visibility = View.VISIBLE
                tv_name_display_bottomsheet.visibility = View.VISIBLE
                img_icon_bottomsheet.visibility = View.VISIBLE
                img_play_bottomsheet.visibility = View.VISIBLE
                img_next_bootomsheet.visibility = View.VISIBLE

                img_favouriteBts.visibility = View.GONE
                img_back_bottomsheet.visibility = View.GONE
                txt_playing_bottomsheet.visibility = View.GONE
            }

            override fun onStateChanged(p0: View, p1: Int) {
                when (p1) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        tv_singer_display_bottomsheet.visibility = View.GONE
                        tv_name_display_bottomsheet.visibility = View.GONE
                        img_icon_bottomsheet.visibility = View.GONE
                        img_play_bottomsheet.visibility = View.GONE
                        img_next_bootomsheet.visibility = View.GONE
                        img_favouriteBts.visibility = View.VISIBLE
                        img_back_bottomsheet.visibility = View.VISIBLE
                        txt_playing_bottomsheet.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }

            }


        })
    }

    fun onClickNavigationItem() {
        nav_View.setOnNavigationItemSelectedListener({
            when (it.itemId) {
                com.zergitas.zermp3.R.id.m_library -> {
                    createFragmentLibrary()
                    return@setOnNavigationItemSelectedListener true
                }
                com.zergitas.zermp3.R.id.m_equalizer -> {
                    createFragmentEquqlizer()
                    return@setOnNavigationItemSelectedListener true
                }
                com.zergitas.zermp3.R.id.m_settings -> {
                    createFragmentSettings()
                    return@setOnNavigationItemSelectedListener true
                }
                com.zergitas.zermp3.R.id.m_favourite -> {
                    createFragmentFavourite()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

    fun createFragmentFavourite() {
        val transaction = manager.beginTransaction()
        val fragment = FragmentFavouriteSongs()
        transaction.replace(com.zergitas.zermp3.R.id.frame_View, fragment)
        transaction.commit()
    }

    fun createFragmentEquqlizer() {
        val transaction = manager.beginTransaction()
        val fragment = FragmentEqualizer()
        transaction.replace(com.zergitas.zermp3.R.id.frame_View, fragment)
        transaction.commit()
    }

    fun createFragmentSettings() {
        val transaction = manager.beginTransaction()
        val fragment = FragmentSettings()
        transaction.replace(com.zergitas.zermp3.R.id.frame_View, fragment)
        transaction.commit()
    }

    fun createFragmentLibrary() {
        val transaction = manager.beginTransaction()
        val fragment = FragmentLibrary()
        transaction.replace(com.zergitas.zermp3.R.id.frame_View, fragment)
        transaction.commit()
    }


}
