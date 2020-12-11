package com.randochat.main.socketserver.serverBehavior

import com.randochat.main.socketserver.dataAccsess.Directory
import com.randochat.main.socketserver.dataAccsess.JsonValues
import com.randochat.main.socketserver.dataAccsess.User
import com.randochat.main.socketserver.messages.Messages
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import javax.json.Json
import javax.json.JsonObject
import javax.json.JsonString
import javax.json.JsonValue
import kotlin.system.exitProcess

//handles intial authorization
class Authorizer(val selector: Selector) {
    private val suspects = HashMap<SocketAddress, User>()


    fun investigateConn(key: SelectionKey){
        val channel = key.channel() as ServerSocketChannel
        val newChan = channel.accept()
        newChan.configureBlocking(false)
        newChan.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE)
        suspects[newChan.remoteAddress] = User(newChan)
    }
    fun attemptValidate(conn: SocketChannel): Boolean{
        //todo, if valid token then assign the user object the accountID?
        val message = ByteBuffer.allocate(1024)
        var length = conn.read(message)
        var token = ""
        if (length >= 0){
            for (i in 0 until length){
                token += message[i].toChar()
            }
            val json = Messages.messageFromJsonStringr(token)
            if (json.equals(JsonObject.EMPTY_JSON_OBJECT)){
                println("handle bad json")
                exitProcess(4)
            }
            //todo lots of this stuff shuold be handled by a validator class. maybe a switch
            //json.toString may be wrapped around qoutes and might break this
            if (json.containsKey("intent") && json.containsKey("token")){
                if (json.get("intent")!! == JsonValues.OPENNEW) {
                    //todo, assuming all tokens are valid for now
                    println("valid token")
                    return true
                }else if (json.get("intent")!! == JsonValues.RECONNECT){
                    // check room id
                    // roomId, check if room is awaiting a reconnect, take over that user object and assign it to this connection
                    var roomID = json.get("roomID").toString()
                    println(roomID)
                    if(Directory.validRoom(roomID)){
                        //room is still alive.
                        //identify user and replace them with a new user object
                        val room = Directory.getRoom(roomID)
                        println(room.connectionStatus)
                        //todo client should have acsess to an account Id and roomId to save the hassle of trying to indentifiy client here

                    }else{
                        //reconnect after room has been killed, treat as a new user?
                    }
                    return true
                }
            }
            else{
                println("invalid token")
                return killSuspect(conn)
            }
        }else if (suspects[conn.remoteAddress]!!.authTimeOut > System.currentTimeMillis()){
            println("timeout")
            return true
        }else{
            return killSuspect(conn)
        }
        return true
    }
    fun killSuspect(conn: SocketChannel): Boolean{
        //todo, deregister with selector
        println("killing " + conn.remoteAddress)
        //maybe keep a count of all failed attempt, and eventually blacklist them?
        conn.close()
        conn.socket().close()
        suspects.remove(conn.remoteAddress)
        return false
    }
    fun authorize(key: SocketAddress): User {
//        println("Accetped")
        Directory.addUser(suspects[key]!!)
        val user = suspects[key]!!
        suspects.remove(key)

        return user
    }
    fun isSuspect(key: SocketAddress): Boolean {
        return suspects[key] != null
    }
}