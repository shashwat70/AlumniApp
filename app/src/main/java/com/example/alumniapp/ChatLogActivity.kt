package com.example.alumniapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_chat_log.view.*
import kotlinx.android.synthetic.main.char_from_row.view.*
import kotlinx.android.synthetic.main.char_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()
    var toUser:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerview_chatlog.adapter = adapter

        toUser = intent.getParcelableExtra<User>("USER")
        if(toUser==null)
            finish()
        supportActionBar?.title = toUser?.firstName+" "+toUser?.lastName

//        val adapter = GroupAdapter<GroupieViewHolder>()
//        recyclerview_chatlog.adapter = adapter
        listenForMessages()
        send_button.setOnClickListener {
            Log.d("Chat", "Attempt send")
            performSendMessage()
        }
    }
    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object:ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if(chatMessage!=null){
                    Log.d("CHAT", chatMessage.text)
                    if(FirebaseAuth.getInstance().uid == chatMessage.fromId)
                        adapter.add(ChatToItem(chatMessage.text))
                    else
                        adapter.add(ChatFromItem(chatMessage.text))
                }
                recyclerview_chatlog.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        })
    }
    private fun performSendMessage() {
        val text = message_edittext.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        if(fromId==null)
            return
        if(toUser==null)
            return
        val toId = toUser?.uid
        if(toId==null)
            return
        val fromReference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(fromReference.key!!, text, fromId, toId, System.currentTimeMillis()/1000)
        fromReference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("CHAT", "Saved chat message ${fromReference.key}")
                message_edittext.text.clear()
                recyclerview_chatlog.scrollToPosition(adapter.itemCount-1)
            }
        toReference.setValue(chatMessage)

        val latestMessageFromReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageFromReference.setValue(chatMessage)

        val latestMessageToReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToReference.setValue(chatMessage)
    }
}





class ChatFromItem(val text: String):Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.char_from_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_from.text = text
    }

}

class ChatToItem(val text: String):Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.char_to_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_to.text = text
    }

}