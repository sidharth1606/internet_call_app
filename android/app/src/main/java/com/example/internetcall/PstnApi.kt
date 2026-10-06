package com.example.internetcall

import okhttp3.*
import org.json.JSONObject

class PstnApi(private val baseUrl:String){
 private val client=OkHttpClient()
 fun placeCall(destination:String,callback:(String)->Unit){
  val body=RequestBody.create("application/json".toMediaType(),JSONObject().put("to",destination).toString())
  val req=Request.Builder().url("$baseUrl/api/pstn/call").post(body).build()
  client.newCall(req).enqueue(object:Callback{
   override fun onFailure(call:Call,e:java.io.IOException){callback("Error: ${e.message}")}
   override fun onResponse(call:Call,response:Response){callback(response.body?.string()?:"No response")}
  })
 }
}
