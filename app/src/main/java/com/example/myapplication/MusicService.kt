package com.example.myapplication

import android.app.ForegroundServiceStartNotAllowedException
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat


class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? =
        null   //поч так, а не просто var mediaPlayer : MediaPlayer

    //lateinit - переменная не инициализирована сейчас,
    // но мы гарантируем, что инициплизируем позже

    private var song = mutableListOf(R.raw.song1, R.raw.song2, R.raw.song3)
    private var posSong = 0
    private val binder = LocalBinder()

    fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "mp3.player",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                context.getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startForeground() {
        try {
            val notification = NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.baseline_music_note_24)
                .setContentTitle("mp3.player")
                .setContentText("Playing music")
                .build()
            ServiceCompat.startForeground(
                /* service = */ this,
                /* id = */ 100, // Cannot be 0
                /* notification = */ notification,
                /* foregroundServiceType = */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                } else {
                    0
                },
            )
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service
                // (e.g. started from bg)
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        Log.d("!!!", "Сервис создан")
        createNotificationChannel(this)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("!!!", "Сервису пришла команда на старт")
        startForeground()
        return super.onStartCommand(intent, flags, startId)

    }

    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("!!!", "Привязываем сервис к main")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("!!!", "Отвязываем сервис от main")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.d("!!!", "Сервис уничтожен")
        super.onDestroy()
    }

    fun playSong() {
//        Log.d("!!!", "Запустили метод playSong")
//        if (mediaPlayer == null) {
//            Log.d("!!!", "Если player пуст")
//            mediaPlayer = MediaPlayer.create(this, song[posSong])
//            Log.d("!!!", "Добавляем в него песню под номером ${song[posSong]}")
//        }
//        Log.d("!!!", "Запускаем музыку")
//        mediaPlayer?.start()

        Log.d("!!!", "Запустили метод playSong")
        Log.d(
            "!!!",
            "Плеер есть, но он не заполнен музыкой в данный момент. Т.е не проигрывает ничего"
        )
        mediaPlayer?.reset()
        Log.d("!!!", "Добавляем в него песню под номером ${song[posSong]}")
        mediaPlayer = MediaPlayer.create(this, song[posSong])
        Log.d("!!!", "Запускаем музыку")
        mediaPlayer?.start()

    }

    fun pauseSong() {
        Log.d("!!!", "Нажали кнопку PAUSE, вызвался метод pauseSong")
        mediaPlayer?.pause()
        Log.d("!!!", "Музыка остановилась")

    }

    fun playTheNextOne() {
        Log.d("!!!", "Нажали кнопку NEXT, вызвался метод playTheNextOne")
        if (mediaPlayer?.isPlaying == true) mediaPlayer?.stop()
        Log.d("!!!", "Если player играет и это верно, останавливаем player")
        if (posSong < (song.size - 1)) {
            Log.d("!!!", " елси номер текущей песни меньше длины массива")
            posSong++
            Log.d("!!!", "Добавляем следующую")
        } else {
            posSong = 0
            Log.d("!!!", "Иначе номер текущей песни равен началу")
        }
        playSong()
        Log.d("!!!", "Играем песню")
    }

    fun playThePreviousOne() {
        Log.d("!!!", "Нажали кнопку Previous, вызвался метод playThePreviousOne")
        if (mediaPlayer?.isPlaying == false) mediaPlayer?.stop()
        Log.d("!!!", "Если плеер не играет, останавливаем player")
        if (posSong > 0) {
            Log.d("!!!", "Если позиция $posSong больше начальной позиции ")
            posSong--
            Log.d("!!!", "Отнимаем на одну, переключаем на предыдущую")
        } else {
            Log.d("!!!", "В другом случае ")
            posSong = song.size - 1
            Log.d("!!!", "Номер текущей позиции $posSong равен позиции последней песни")
        }
        playSong()
        Log.d("!!!", "Играем музыку")

    }

}