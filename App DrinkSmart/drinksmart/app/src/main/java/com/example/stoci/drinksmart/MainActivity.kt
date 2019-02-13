package com.example.stoci.drinksmart

import android.content.Intent
import android.drm.DrmStore
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.ActionBar
import android.widget.Button
import android.support.v7.widget.Toolbar

class MainActivity : AppCompatActivity() {

    lateinit var btn_search:Button
    lateinit var btn_favorites:Button
    lateinit var btn_top10:Button
    lateinit var btn_DrinkFinder:Button
    lateinit var myToolbar:Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_search = findViewById(R.id.btn_search)
        btn_favorites = findViewById(R.id.btn_favorites)
        btn_top10 = findViewById(R.id.btn_top10)
        btn_DrinkFinder = findViewById(R.id.btnDrinkFinder)
        myToolbar =findViewById(R.id.myToolbar)

        setSupportActionBar(myToolbar)




        btn_search.setOnClickListener{

            val intent: Intent = Intent(applicationContext,SearchActivity::class.java)

            startActivity(intent)
        }

        btn_favorites.setOnClickListener{

            val intent: Intent = Intent(applicationContext,FavoritesActivity::class.java)

            startActivity(intent)
        }

        btn_top10.setOnClickListener{

            val intent: Intent = Intent(applicationContext,Top10Activity::class.java)

            startActivity(intent)
        }

        btn_DrinkFinder.setOnClickListener{

            val intent: Intent = Intent(applicationContext,DrinkFinderActivity::class.java)

            startActivity(intent)
        }
    }
}


