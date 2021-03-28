package com.example.objectdetection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Maps : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        var gotoLearn = this.findViewById<Button>(R.id.gotoLearn)
        var gotoTrans = this.findViewById<Button>(R.id.gotoTrans)
        gotoLearn.setOnClickListener {
            val intent = Intent(this, Learn::class.java)
            startActivity(intent)
        }
        gotoTrans.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}