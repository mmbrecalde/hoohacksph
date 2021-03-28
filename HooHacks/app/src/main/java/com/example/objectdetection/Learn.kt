package com.example.objectdetection

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Learn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        val text = findViewById<TextView>(R.id.textView)
        text.movementMethod = LinkMovementMethod.getInstance()

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