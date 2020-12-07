package com.randochat.main.socketserver

import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentHashMap

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
            if (token == "HELLO"){
//                println("valid token")
                return true
            }else{
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