package com.example.internetcall

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import okhttp3.*

class MainActivity : AppCompatActivity() {
    private val SIGNALING_HOST = "ws://192.168.1.10:8000"
    private lateinit var userId: EditText
    private lateinit var peerId: EditText
    private lateinit var status: TextView
    private var socket: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(32,48,32,32)}
        userId=EditText(this).apply{hint="Your user ID"}
        peerId=EditText(this).apply{hint="Friend user ID"}
        status=TextView(this).apply{text="Ready"}
        val connect=Button(this).apply{text="Connect"}
        val call=Button(this).apply{text="Call App User"}
        val hang=Button(this).apply{text="Hang Up"}
        root.addView(userId);root.addView(peerId);root.addView(status);root.addView(connect);root.addView(call);root.addView(hang)
        setContentView(root)
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.RECORD_AUDIO),100)
        connect.setOnClickListener{connectSignaling()}
        call.setOnClickListener{send(mapOf("type" to "ringing","to" to peerId.text.toString().trim()))}
        hang.setOnClickListener{send(mapOf("type" to "hangup","to" to peerId.text.toString().trim()))}
    }
    private fun connectSignaling(){
        val me=userId.text.toString().trim(); if(me.isEmpty()){status.text="Enter your user ID";return}
        val req=Request.Builder().url("$SIGNALING_HOST/ws/$me").build()
        socket=OkHttpClient().newWebSocket(req,object:WebSocketListener(){
            override fun onOpen(webSocket:WebSocket,response:Response){runOnUiThread{status.text="Signaling connected"}}
            override fun onMessage(webSocket:WebSocket,text:String){runOnUiThread{status.text="Received: $text"}}
            override fun onFailure(webSocket:WebSocket,t:Throwable,response:Response?){runOnUiThread{status.text="Error: ${t.message}"}}
        })
    }
    private fun send(values:Map<String,String>){
        val to=values["to"] ?: return
        val type=values["type"] ?: return
        socket?.send("{\"type\":\"$type\",\"to\":\"$to\"}")
    }
}
