package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder


class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? =
        null   //поч так, а не просто var mediaPlayer : MediaPlayer

    //lateinit - переменная не инициализирована сейчас,
    // но мы гарантируем, что инициплизируем позже

    private var song = mutableListOf(R.raw.song1, R.raw.song2, R.raw.song3)
    private var posSong = 0
    private val binder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun playSong() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, song[posSong])
        }
        mediaPlayer?.start()

    }

    fun pauseSong() {
        mediaPlayer?.pause()

    }

    fun playTheNextOne() {
        if (mediaPlayer?.isPlaying == true) mediaPlayer?.stop()
        if (posSong < (song.size - 1)) {
            posSong++
        } else {
            posSong = 0
        }
        playSong()
    }

    fun playThePreviousOne() {
        if (mediaPlayer?.isPlaying == false) mediaPlayer?.stop()
        if (posSong > 0) {
            posSong--
        } else {
            posSong = song.size - 1
        }
        playSong()

    }
}