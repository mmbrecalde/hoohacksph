package com.example.objectdetection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Learn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        var gotoMAP = this.findViewById<Button>(R.id.gotoMAP)
        var gotoTrans = this.findViewById<Button>(R.id.gotoTrans)
        gotoMAP.setOnClickListener {
            val intent = Intent(this, Maps::class.java)
            startActivity(intent)
        }
        gotoTrans.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}