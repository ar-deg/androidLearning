package com.example.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/*
Создать музыкальный плеер. При разработке использовать Activity и Service (любой, можно сочетать
разные варианты) Плеер должен иметь работоспособные кнопки Play/Pause/Next/Previous
отображаемые на активити. Воспроизводить музыку, останавливать ее и переключать песни. Плеер должен
уметь работать в фоне, т.е когда приложение свернуто.
Для усложнения: при работе в фоне функционал плеера (кнопки) должны отображаться
в уведомлении (notification) и быть работоспособным.

музыка должна идти листом?
клики кнопок в активити onCreate, но реализация работы в Service. onBind Foreground
связь через intent
добавить музыку
добавить одну картинку на все песни
сикбар должен идти совместно с песней
 */
class MainActivity : AppCompatActivity() {
    private lateinit var btnPlay: Button
    private lateinit var btnStop: Button
    private lateinit var btnPrevious: Button
    private lateinit var btnNext: Button
    private var musicService: MusicService? = null
    private var isBound: Boolean = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            musicService = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        btnPlay = findViewById<Button>(R.id.btnPlay)
        btnStop = findViewById<Button>(R.id.btnStop)
        btnPrevious = findViewById<Button>(R.id.btnPrevious)
        btnNext = findViewById<Button>(R.id.btnNext)


        btnPlay.setOnClickListener {
            musicService?.playSong()


        }
        btnStop.setOnClickListener {
            musicService?.pauseSong()

        }
        btnPrevious.setOnClickListener {
            musicService?.playThePreviousOne()

        }
        btnNext.setOnClickListener {
            musicService?.playTheNextOne()

        }

    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService.
        Intent(this, MusicService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        musicService = null
        isBound = false
    }
}