package com.example.boxingtimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var rounds = 3;
    var roundTime = 3;
    var restTime = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val roundsTextView = findViewById<TextView>(R.id.roundNumberTextView)
        val timeTextView = findViewById<TextView>(R.id.timeNumberTextView)
        val restTextView = findViewById<TextView>(R.id.restNumberTextView)

        roundsTextView.text = rounds.toString()
        timeTextView.text = roundTime.toString()
        restTextView.text = restTime.toString()
    }

    fun onPlusMinusButClick(view: View) {
        val roundsTextView = findViewById<TextView>(R.id.roundNumberTextView)
        val timeTextView = findViewById<TextView>(R.id.timeNumberTextView)
        val restTextView = findViewById<TextView>(R.id.restNumberTextView)

        when(view.id){
            R.id.restPlusButton -> if(restTime < 10) restTime++
            R.id.roundPlusButton -> if(rounds < 15) rounds++
            R.id.timePlusButton -> if(roundTime < 10) roundTime++

            R.id.restMinusButton -> if(restTime > 1) restTime--
            R.id.roundMinusButton2 -> if(rounds > 1) rounds--
            R.id.timeMinusButton -> if(roundTime > 1) roundTime--
        }
        roundsTextView.text = rounds.toString()
        timeTextView.text = roundTime.toString()
        restTextView.text = restTime.toString()
    }

    fun onStartClick(view: View) {
        val intent : Intent = Intent(this, TimerActivity::class.java)
        intent.putExtra("rounds", rounds)
        intent.putExtra("roundTime", roundTime)
        intent.putExtra("restTime", restTime)
        startActivity(intent)
    }


}
