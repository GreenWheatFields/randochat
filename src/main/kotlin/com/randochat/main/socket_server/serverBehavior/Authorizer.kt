package com.randochat.main.socket_server.serverBehavior

import com.randochat.main.socket_server.dataAccsess.Directory
import com.randochat.main.socket_server.dataAccsess.JsonValues
import com.randochat.main.socket_server.dataAccsess.User
import com.randochat.main.socket_server.messages.Messages
import com.randochat.main.spring_app.utility.Token
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import javax.json.JsonObject

//handles intial authorization
class Authorizer(val selector: Selector) {
    //todo, blacklist ips?
    private val suspects = HashMap<SocketAddress, User>()
    var lastSweep = System.currentTimeMillis()

    fun investigateConn(key: SelectionKey){
        val channel = key.channel() as ServerSocketChannel
        val newChan = channel.accept()
        newChan.configureBlocking(false)
        newChan.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE)
        suspects[newChan.remoteAddress] = User(newChan)
    }
    fun attemptValidate(conn: SocketChannel): Boolean{
        val message = ByteBuffer.allocate(1024)
        var length = conn.read(message)
        var incommingString = ""
        if (length >= 0){
            for (i in 0 until length){
                incommingString += message[i].toChar()
            }
            val json = Messages.messageFromJsonString(incommingString)
            if (json.equals(JsonObject.EMPTY_JSON_OBJECT)){
                return false
            }

            if (suspects.contains(conn.remoteAddress) && suspects[conn.remoteAddress]!!.authTimeOut > System.currentTimeMillis()) {
                if (json.containsKey("intent") && json.containsKey("token")) {
                    val userToken = Token.checkTokenValid(json["token"].toString()) ?: return killSuspect(conn)
                    if (json.get("intent")!! == JsonValues.OPENNEW) {
                        suspects[conn.remoteAddress]!!.token = userToken
                        return true
                        }
                } else {
                return killSuspect(conn)

                }
            }else{
                return false
            }
        }else{
            return false
        }
        return false
    }
    fun killSuspect(conn: SocketChannel): Boolean{
        println("killing " + conn.remoteAddress)
        //maybe keep a count of all failed attempt, and eventually blacklist them?
        suspects.remove(conn.remoteAddress)
        conn.close()
        conn.socket().close()
        return false
    }
    fun authorize(key: SocketAddress): User {
        Directory.addUser(suspects[key]!!)
        val user = suspects[key]!!
        suspects.remove(suspects[key]!!.address)
        return user
    }
    fun sweep(){
        val killList = ArrayList<User>()
        for (suspect in suspects.values){
            if (!suspect.isAuthorized && suspect.authTimeOut < System.currentTimeMillis()){
                killList.add(suspect)
            }
        }
        killList.forEach { killSuspect(it.socketChannel) }
        lastSweep = System.currentTimeMillis() + 10_000
    }
    fun isSuspect(key: SocketAddress): Boolean {
        return suspects[key] != null
    }
}