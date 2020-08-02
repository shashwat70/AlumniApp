package com.example.alumniapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.LinearLayout
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
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.alumniapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_inbox.*
import kotlinx.android.synthetic.main.latest_message_row.view.*

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var linearLayout: LinearLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousMenuItem:MenuItem?=null
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerLayout)
        linearLayout = findViewById(R.id.linearLayout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationView)
        setUpToolbar()
        supportActionBar?.title="Inbox"
       // navigationView.setCheckedItem(R.id.nav_item_one)
        navigationView.setCheckedItem(R.id.nav_item_two)
        navigationView.getMenu().getItem(1).setChecked(true);
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
                    supportActionBar?.title="My Profile"
                  //  navigationView.setCheckedItem(R.id.nav_item_one)
                    drawerLayout.closeDrawers()
                }

                R.id.nav_item_two->{
                    navigationView.setCheckedItem(R.id.nav_item_two)
                    supportActionBar?.title="Inbox"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_three->{
                    supportActionBar?.title="Find People"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_four->{

                    supportActionBar?.title="Contacts"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_five->{

                    supportActionBar?.title="News and Updates"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_six->{

                    supportActionBar?.title="Credits"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_seven->{

                    supportActionBar?.title="News and Updates"
                    drawerLayout.closeDrawers()
                }
                R.id.nav_item_eight->{

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

        recyclerview_latest_messages.adapter=adapter
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this,
        DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)

            val row = item as LatestMessageRow
            intent.putExtra("USER", row.chatPartnerUser)
            startActivity(intent)
        }
        listenForLatestMessages()

    }



    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessageRow(it))
        }
    }
    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)?:return
                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)?:return
                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
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
        supportActionBar?.title="Inbox"
        navigationView.setCheckedItem(R.id.nav_item_one)
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers()

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
