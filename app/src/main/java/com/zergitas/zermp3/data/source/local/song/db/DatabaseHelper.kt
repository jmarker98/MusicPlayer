package com.zergitas.zermp3.data.source.local.song.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.zergitas.zermp3.data.model.Album
import com.zergitas.zermp3.data.model.Playlist
import com.zergitas.zermp3.data.model.Singer
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.screen.main.MainActivity

class DatabaseHelper(
    var context: Context,
    DATABASE_NAME: String = "MyDb",
    DATABASE_VERSION: Int = 4
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    var allSongs = ArrayList<Song>()
    var listIdsong = ArrayList<Long>()
    var listPlaylist = ArrayList<Playlist>()
    var listSinger = ArrayList<String>()
    var listSingerfromDB = ArrayList<Singer>()
    var listSongBySinger = ArrayList<Song>()
    var listFavourite = ArrayList<Song>()
    var listIdAlbum = ArrayList<Long>()
    var albumName: String = ""
    var listAlbum = ArrayList<Album>()
    var listSongByIdAlbum = ArrayList<Song>()

    override fun onCreate(db: SQLiteDatabase?) {
        val createtable = "CREATE TABLE IF NOT EXISTS tbl_song (\n" +
                "    id       LONG    PRIMARY KEY\n" +
                "                     NOT NULL,\n" +
                "    idAlbum  LONG     NOT NULL,\n" +
                "    name     STRING,\n" +
                "    singer     STRING,\n" +
                "    path     STRING,\n" +
                "    duration LONG,\n" +
                "    date     LONG,\n" +
                "    size     LONG,\n" +
                "    album     String,\n" +
                "    liked    BOOLEAN NOT NULL\n" +
                ");\n"

        val create_playlist = "CREATE TABLE tbl_playlists (\n" +
                "    id_playlist INT PRIMARY KEY \n" +
                "                        NOT NULL,\n" +
                "    name        STRING  NOT NULL\n" +
                ");"
        val create_playlist_song = "CREATE TABLE tbl_playlist_songs (\n" +
                "    id          INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    id_song     LONG     NOT NULL,\n" +
                "    id_playlist INT     NOT NULL\n" +
                ");\n"

        val create_singer = "CREATE TABLE tbl_singer (\n" +
                "    id   INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    name NONE    NOT NULL\n" +
                ");\n"

        val create_album = "CREATE TABLE tbl_album (\n" +
                "    id   LONG PRIMARY KEY,\n" +
                "    album String    NOT NULL\n" +
                ");\n"

        db?.execSQL(createtable)
        db?.execSQL(create_playlist)
        db?.execSQL(create_playlist_song)
        db?.execSQL(create_singer)
        db?.execSQL(create_album)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS tbl_song")
        db?.execSQL("DROP TABLE IF EXISTS tbl_playlists")
        db?.execSQL("DROP TABLE IF EXISTS tbl_playlist_songs")
        db?.execSQL("DROP TABLE IF EXISTS tbl_singer")
        db?.execSQL("DROP TABLE IF EXISTS tbl_album")
        onCreate(db)
    }

    fun inset_playlist(id_playlist: Int, name: String) {
        writableDatabase.execSQL(
            "INSERT INTO tbl_playlists (\n" +
                    "                         id_playlist,\n" +
                    "                         name\n" +
                    "                     )\n" +
                    "                     VALUES (\n" +
                    "                         " + id_playlist + ",\n" +
                    "                         '" + name + "'\n" +
                    "                     );\n"
        )
        writableDatabase.close()

    }

    fun inset_playlist_song(id_song: Long, id_playlist: Int) {
        writableDatabase.execSQL(
            "INSERT INTO tbl_playlist_songs (\n" +
                    "                             id_song,\n" +
                    "                             id_playlist\n" +
                    "                         )\n" +
                    "                         VALUES (\n" +
                    "                             '" + id_song + "',\n" +
                    "                             '" + id_playlist + "'\n" +
                    "                         );"
        )
        writableDatabase.close()

    }

    fun insertData(
        id: Long,
        idAlbum: Long,
        name: String,
        singer: String,
        path: String,
        duration: Long,
        date: Long,
        size: Long,
        album: String,
        liked: Boolean
    ) {
        val insertdata = "INSERT INTO tbl_song (\n" +
                "                     id,\n" +
                "                     idAlbum,\n" +
                "                     name,\n" +
                "                     singer,\n" +
                "                     path,\n" +
                "                     duration,\n" +
                "                     date,\n" +
                "                     size,\n" +
                "                     album,\n" +
                "                     liked\n" +
                "                 )\n" +
                "                 VALUES (\n" +
                "                     " + id + ",\n" +
                "                     " + idAlbum + ",\n" +
                "                     \"" + name + "\",\n" +
                "                     \"" + singer + "\",\n" +
                "                     \"" + path + "\",\n" +
                "                     " + duration + ",\n" +
                "                     " + date + ",\n" +
                "                    " + size + ",\n" +
                "                    \"" + album + "\",\n" +
                "                     \"" + liked + "\"\n" +
                "                 );"
        writableDatabase.execSQL(insertdata)
    }

    fun updateData(id: Long, liked: Boolean) {
        val update = "UPDATE tbl_song\n" +
                "   SET \n" +
                "       liked = '" + liked + "'\n" +
                " WHERE id = '" + id + "' \n" +
                "      "
        writableDatabase.execSQL(update)
    }

    fun getData(): ArrayList<Song> {
        val select = "SELECT *\n" +
                "  FROM tbl_song \n"
        val cursor = writableDatabase.rawQuery(select, null)
        if (cursor.moveToFirst()) {
            do {
                val allSong = Song(
                    cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getLong(cursor.getColumnIndex("idAlbum")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("singer")),
                    cursor.getString(cursor.getColumnIndex("path")),
                    null,
                    cursor.getLong(cursor.getColumnIndex("duration")),
                    cursor.getLong(cursor.getColumnIndex("date")),
                    cursor.getLong(cursor.getColumnIndex("size")),
                    cursor.getString(cursor.getColumnIndex("album")),
                    cursor.getString(cursor.getColumnIndex("liked")).equals("true")
                )

                Log.d("ab", "song liked " + cursor.getString(cursor.getColumnIndex("liked")))

                allSongs.add(allSong)
            } while (cursor.moveToNext())
        }
        writableDatabase.close()
        return allSongs
    }

    fun getFavouriteSongs(): ArrayList<Song> {
        val select = "select * from tbl_song where liked = 'true' "
        val cursor = writableDatabase.rawQuery(select, null)
        if (cursor.moveToFirst()) {
            do {
                val favouriteSong = Song(
                    cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getLong(cursor.getColumnIndex("idAlbum")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("singer")),
                    cursor.getString(cursor.getColumnIndex("path")),
                    null,
                    cursor.getLong(cursor.getColumnIndex("duration")),
                    cursor.getLong(cursor.getColumnIndex("date")),
                    cursor.getLong(cursor.getColumnIndex("size")),
                    cursor.getString(cursor.getColumnIndex("album")),
                    cursor.getString(cursor.getColumnIndex("liked")).equals("true")
                )

                Log.d("ab", "song liked " + cursor.getString(cursor.getColumnIndex("liked")))

                listFavourite.add(favouriteSong)
            } while (cursor.moveToNext())
        }
        writableDatabase.close()
        return listFavourite
    }

    fun getSongById(id_song: Long): Song {
        val select = "SELECT * FROM tbl_song where id=$id_song"
        val cursor = writableDatabase.rawQuery(select, null)
        if (cursor.moveToFirst()) {
            do {
                val songById = Song(
                    cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getLong(cursor.getColumnIndex("idAlbum")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("singer")),
                    cursor.getString(cursor.getColumnIndex("path")),
                    null,
                    cursor.getLong(cursor.getColumnIndex("duration")),
                    cursor.getLong(cursor.getColumnIndex("date")),
                    cursor.getLong(cursor.getColumnIndex("size")),
                    cursor.getString(cursor.getColumnIndex("album")),
                    cursor.getString(cursor.getColumnIndex("liked")).equals("true")
                )
                return songById
            } while (cursor.moveToNext())
        }
        writableDatabase.close()
        return null!!
    }

    fun getIdsong(id_playlist: Int): ArrayList<Long> {
        listIdsong = ArrayList()
        val select = "SELECT id_song FROM tbl_playlist_songs where id_playlist = $id_playlist ;"
        val cursor = readableDatabase.rawQuery(select, null)
        if (cursor.moveToFirst()) {
            do {
                val idSong = cursor.getLong(cursor.getColumnIndex("id_song"))
                listIdsong.add(idSong)
            } while (cursor.moveToNext())
        }
        writableDatabase.close()
        return listIdsong
    }

    fun countSongs(id_playlist: Int): Int {
        var countquery =
            readableDatabase.rawQuery("select * from tbl_playlist_songs where id_playlist = $id_playlist", null)
        val count: Int = countquery.count
        return count
    }

    fun getAllPlaylist(): ArrayList<Playlist> {
        listPlaylist = ArrayList()
        val select = "select * from tbl_playlists"
        val cursor = readableDatabase.rawQuery(select, null)
        if (cursor.moveToFirst()) {
            do {
                var playlist = Playlist(
                    cursor.getInt(cursor.getColumnIndex("id_playlist")),
                    cursor.getString(cursor.getColumnIndex("name"))
                )
                listPlaylist.add(playlist)
            } while (cursor.moveToNext())
        }
        writableDatabase.close()
        return listPlaylist
    }

    fun deletePlaylist(id_playlist: Int) {
        writableDatabase.execSQL("delete from tbl_playlists where id_playlist=$id_playlist")
    }

    fun deleteAllPlaylist() {
        writableDatabase.execSQL("delete from tbl_playlists")
    }

    fun updatePlaylist(newName: String, id_playlist: Int) {
        writableDatabase.execSQL(
            "UPDATE tbl_playlists\n" +
                    "   SET  name = \"" + newName + "\"\n" +
                    " WHERE id_playlist = " + id_playlist + ";\n"
        )
    }

    fun getAllSinger(): ArrayList<String> {
        listSinger = ArrayList()
        val select = "select DISTINCT singer from tbl_song;"
        val cursor = readableDatabase.rawQuery(select, null)
        if (cursor != null) {
            cursor.moveToFirst()
            do {
                Log.e("size", cursor.count.toString())
                val singer: String = cursor.getString(cursor.getColumnIndex("singer"))
                listSinger.add(singer)
            } while (cursor.moveToNext())
        }
        writableDatabase.close()
        return listSinger
    }

    fun inserSingertoDb(name: String) {
        writableDatabase.execSQL(
            "INSERT INTO tbl_singer (name)\n" +
                    "                       VALUES (\"" + name + "\");\n"
        )
    }

    fun getSingerfromDb(): ArrayList<Singer> {
        listSingerfromDB = ArrayList()
        val select = "SELECT * FROM tbl_singer"
        val cursor = readableDatabase.rawQuery(select, null)
        if (cursor != null) {
            cursor.moveToFirst()
            do {
                var singer = Singer(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name"))
                )
                listSingerfromDB.add(singer)
            } while (cursor.moveToNext())

        }
        writableDatabase.close()
        return listSingerfromDB
    }

    fun getSongBySinger(singer: String): ArrayList<Song> {
        listSongBySinger = ArrayList()
        val select = "SELECT * FROM tbl_song where singer = '" + singer + "';"
        val cursor = writableDatabase.rawQuery(select, null)
        if (cursor.moveToFirst()) {
            do {
                Log.d("getsinger", "ok")
                val songBySinger = Song(
                    cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getLong(cursor.getColumnIndex("idAlbum")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("singer")),
                    cursor.getString(cursor.getColumnIndex("path")),
                    null,
                    cursor.getLong(cursor.getColumnIndex("duration")),
                    cursor.getLong(cursor.getColumnIndex("date")),
                    cursor.getLong(cursor.getColumnIndex("size")),
                    cursor.getString(cursor.getColumnIndex("album")),
                    cursor.getString(cursor.getColumnIndex("liked")).equals(true)
                )
                listSongBySinger.add(songBySinger)
            } while (cursor.moveToNext())
        }
        writableDatabase.close()
        return listSongBySinger
    }

    fun checkCountofTbl_singer(): Int {
        val check: Cursor = writableDatabase.rawQuery("select  * from tbl_singer", null)
        Log.d("rowcount", check.count.toString())
        return check.count
    }

    fun checkCountofTbl_Song(): Int {
        val check: Cursor = writableDatabase.rawQuery("select * from tbl_song", null)
        Log.d("songcount", check.count.toString())
        return check.count
    }

    fun getIdAlumfromtbl_song(): ArrayList<Long> {
        listIdAlbum = ArrayList()
        val cursor = readableDatabase.rawQuery("SELECT DISTINCT idAlbum FROM tbl_song", null)
        if (cursor != null) {
            cursor.moveToFirst()
            do {
                val idAlbum: Long = cursor.getLong(cursor.getColumnIndex("idAlbum"))
                Log.d("checkidalbum", idAlbum.toString())
                listIdAlbum.add(idAlbum)
            } while (cursor.moveToNext())
        }
        writableDatabase.close()
        return listIdAlbum

    }

    fun getAlbumfromIdAlbum(idAlbum: Long): String {
        albumName = String()
        val cursor = readableDatabase.rawQuery("select distinct album from tbl_song where idAlbum = $idAlbum", null)
        if (cursor != null) {
            cursor.moveToFirst()
            do {
                val album = cursor.getString(cursor.getColumnIndex("album"))
                albumName = album.toString()
                Log.d("NAMEAL", albumName)
            } while (cursor.moveToNext())
        }
        return albumName
    }

    fun insertAlbum(idAlbum: Long, albumName: String) {
        writableDatabase.rawQuery("insert into tbl_album(id,album) values ($idAlbum,\"" + albumName + "\")", null)

    }

    fun getListAlbumfrom_tbl_album(): ArrayList<Album> {
        listAlbum = ArrayList()
        val select = "select * from tbl_album"
        val cursor = readableDatabase.rawQuery(select, null)
        Log.d("readData", "1")
        if (cursor != null) {
            cursor.moveToFirst()
            do {
                val album = Album(
                    cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("album"))
                )
                listAlbum.add(album)
                Log.d("readData", listAlbum.size.toString())

            } while (cursor.moveToNext())
        }
        return listAlbum
    }

    fun getSongByIdAlbums(idAlbum: Long): ArrayList<Song> {
        listSongByIdAlbum = ArrayList()
        var select = "SELECT *\n" +
                "  FROM tbl_song where idAlbum = $idAlbum;\n"
        var cursor = readableDatabase.rawQuery(select, null)
        if (cursor != null) {
            cursor.moveToFirst()
            do {
                val songByIdalbum = Song(
                    cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getLong(cursor.getColumnIndex("idAlbum")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("singer")),
                    cursor.getString(cursor.getColumnIndex("path")),
                    null,
                    cursor.getLong(cursor.getColumnIndex("duration")),
                    cursor.getLong(cursor.getColumnIndex("date")),
                    cursor.getLong(cursor.getColumnIndex("size")),
                    cursor.getString(cursor.getColumnIndex("album")),
                    cursor.getString(cursor.getColumnIndex("liked")).equals(true)
                )
                listSongByIdAlbum.add(songByIdalbum)
            } while (cursor.moveToNext())
        }
        return listSongByIdAlbum
    }

}


