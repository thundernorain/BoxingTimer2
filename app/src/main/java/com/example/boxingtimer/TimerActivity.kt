package com.example.boxingtimer

import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import java.util.*

class TimerActivity : AppCompatActivity() {
    lateinit var runnable : Runnable
    var roundsMax = 0;
    var seconds = 0
    var secondsRound = 0
    var secondsRest = 0
    var rounds = 1
    var lastState = 0

    lateinit var soundPool : SoundPool
    val MAX_STREAMS = 5

    var soundIdKnock = 0
    var soundIdGong = 0

    var streamIdKnock = 0
    var streamIdGong = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        soundPool = SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0)
        soundIdGong = soundPool.load(baseContext, R.raw.gong, 1)
        soundIdKnock = soundPool.load(baseContext, R.raw.knock, 1)

        val intent : Intent = getIntent()
        secondsRound = intent.getIntExtra("roundTime", 3) * 60
        secondsRest = intent.getIntExtra("restTime", 1) * 60
        roundsMax = intent.getIntExtra("rounds", 3)

        setTimer(5, 0)

        val timerTextView= findViewById<TextView>(R.id.timerTextView)
        var minutes = (seconds%3600)/60
        var time : String = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        timerTextView.text = time

        val roundTextView = findViewById<TextView>(R.id.roundTextView)
        roundTextView.text = "Round " + rounds + "/" + roundsMax

        runTimer()
    }

    fun setTimer(seconds : Int, state : Int){ // states : 0 - ready, 1 - boxing, 2 - rest
        val timerState = findViewById<TextView>(R.id.timerStateTextView)

        val roundTextView = findViewById<TextView>(R.id.roundTextView)
        roundTextView.text = "Round " + rounds + "/" + roundsMax

        when(state){
            0 -> timerState.text = getString(R.string.wait)
            1 -> timerState.text = getString(R.string.boxing)
            2 -> timerState.text = getString(R.string.rest)
        }
        this.seconds = seconds
    }

    fun runTimer(){
        val handler = Handler()

        runnable = Runnable {
            val timerTextView = findViewById<TextView>(R.id.timerTextView)
            var minutes = (seconds % 3600) / 60
            var secs = seconds%60
            var state = 0
            var time: String = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs)
            timerTextView.text = time

            if (seconds > 0){
                seconds--

                if(seconds == 10) {
                    soundPool?.pause(soundIdGong)
                    soundPool?.play(soundIdKnock, 1F, 1F, 0, 0, 1F)
                }
            }
            else {
                if (rounds <= roundsMax) {
                    if (lastState == 0) {
                        state = 2
                        lastState = state
                    }
                    if (lastState == 1) {
                        if(rounds < roundsMax) rounds++
                        else finish()
                        soundPool?.pause(soundIdKnock)
                        soundPool?.play(soundIdGong, 1F, 1F, 0, 0, 1F)
                        state = 2
                        seconds = secondsRest
                    }
                    if (lastState == 2) {
                        soundPool?.pause(soundIdKnock)
                        soundPool?.play(soundIdGong, 1F, 1F, 0, 0, 1F)
                        state = 1
                        seconds = secondsRound
                    }
                    lastState = state
                    setTimer(seconds, state)
                } else finish()
            }
            handler.postDelayed(runnable,1000)
        }
        handler.postDelayed(runnable,1000)
    }

    fun onStopClick(view: View) {
        finish()
    }
}
