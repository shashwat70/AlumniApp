package com.example.alumniapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        val inflater:MenuInflater=menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item:MenuItem) : Boolean{
        when(item.itemId){
            R.id.search -> {
                Toast.makeText(this@MainActivity, "search", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                return true
            }
            else -> return false
        }
    }
}