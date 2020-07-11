package com.example.alumniapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AlertDialog.Builder
//import androidx.appcompat.widget.SearchView
import android.widget.SearchView
import android.widget.Toast
import com.example.alumniapp.R
import com.google.firebase.auth.FirebaseAuth

//import com.example.alumniapp.fragment.*


class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousMenuItem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        setUpToolbar()
        openInbox()
        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerLayout,R.string.open_drawer,R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem != null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when(it.itemId)
            {
                R.id.nav_item_one->{
                    openInbox()
                    drawerLayout.closeDrawers()

                }

                /* R.id.nav_item_one->{
                     supportFragmentManager.beginTransaction().replace(R.id.frame,
                         ProfileFragment()
                     ).commit()
                     supportActionBar?.title="Profile"
                     drawerLayout.closeDrawers()
                 }*/

                R.id.nav_item_two->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,
                        InboxFragment()
                    ).commit()
                    supportActionBar?.title="Inbox"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_three->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,
                        FindFragment()
                    ).commit()
                    supportActionBar?.title="Find People"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_four->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,
                        ContactsFragment()
                    ).commit()
                    supportActionBar?.title="Contacts"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_five->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,
                        NewsFragment()
                    ).commit()
                    supportActionBar?.title="News and Updates"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_six->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,
                        CreditsFragment()
                    ).commit()
                    supportActionBar?.title="Credits"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_seven->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,
                        AboutFragment()
                    ).commit()
                    supportActionBar?.title="News and Updates"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_eight->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,
                        IBFragment()
                    ).commit()
                    supportActionBar?.title="IIT Bhilai"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_nine->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        //supportActionBar?.title="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        when(id){
            R.id.new_message -> {
                Toast.makeText(this, "New Message", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, NewMessageActivity::class.java)
                startActivity(intent)
            }
        }
        if(id==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun  openInbox()
    {
        val fragment=InboxFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()
        supportActionBar?.title="Inbox"
        navigationView.setCheckedItem(R.id.nav_item_one)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when(frag){
            !is InboxFragment -> openInbox()

            else -> super.onBackPressed()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchViewItem = menu.findItem(R.id.search_icon)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = searchViewItem.actionView as SearchView
        searchView.setQueryHint("Search...")
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(true)
        return true
    }






}
//
//package com.example.alumniapp
//
//import android.app.SearchManager
//import android.content.Context
//import android.content.Intent
//import android.content.res.Configuration
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuInflater
//import android.view.MenuItem
//import android.widget.SearchView
//import android.widget.Toast
//import androidx.appcompat.app.ActionBarDrawerToggle
//import androidx.appcompat.widget.Toolbar
//import androidx.core.view.GravityCompat
//import androidx.drawerlayout.widget.DrawerLayout
//import com.google.android.material.navigation.NavigationView
//
//class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
//
//    private lateinit var drawer: DrawerLayout
//    private lateinit var toggle: ActionBarDrawerToggle
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
//        setSupportActionBar(toolbar)
//
//        drawer = findViewById(R.id.drawer_layout)
//
//        val navigationView: NavigationView = findViewById(R.id.nav_view)
//        navigationView.setNavigationItemSelectedListener(this)
//
//        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer.addDrawerListener(toggle)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeButtonEnabled(true)
//    }
//
////    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
////        val inflater:MenuInflater=menuInflater
////        inflater.inflate(R.menu.main_menu,menu)
////        return true
////    }
//
//    override fun onOptionsItemSelected(item:MenuItem) : Boolean{
//        when(item.itemId){
//            R.id.search -> {
//                Toast.makeText(this@MainActivity, "search", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
//                return true
//            }
//            else -> return false
//        }
//    }
//
//    override fun onPostCreate(savedInstanceState: Bundle?) {
//        super.onPostCreate(savedInstanceState)
//        toggle.syncState()
//    }
//
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        toggle.onConfigurationChanged(newConfig)
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.nav_item_one -> Toast.makeText(this, "Clicked item one", Toast.LENGTH_SHORT).show()
//            R.id.nav_item_two -> Toast.makeText(this, "Clicked item two", Toast.LENGTH_SHORT).show()
//            R.id.nav_item_three -> Toast.makeText(this, "Clicked item three", Toast.LENGTH_SHORT).show()
//            R.id.nav_item_four -> Toast.makeText(this, "Clicked item four", Toast.LENGTH_SHORT).show()
//        }
//        drawer.closeDrawer(GravityCompat.START)
//        return true
//    }
//
//    override fun onBackPressed() {
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val menuInflater = menuInflater
//        menuInflater.inflate(R.menu.main_menu, menu)
//        val searchViewItem = menu.findItem(R.id.search_icon)
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView: SearchView = searchViewItem.actionView as SearchView
//        searchView.setQueryHint("Search...")
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//        searchView.setIconifiedByDefault(true)
//        return true
//    }
//
////    override fun onOptionsItemSelected(item: MenuItem): Boolean {
////        if (toggle.onOptionsItemSelected(item)) {
////            return true
////        }
////        return super.onOptionsItemSelected(item)
////    }
//}