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
    private val suspects = HashMap<SocketAddress, Long>()


    fun investigateConn(key: SelectionKey){
        val channel = key.channel() as ServerSocketChannel
        val newChan = channel.accept()
        newChan.configureBlocking(false)
        newChan.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE)
        suspects[newChan.remoteAddress] = System.currentTimeMillis() + 1000
    }
    fun attemptValidate(conn: SocketChannel){
        //check if a message exist. if it does, check if it is valid. if it is valid welcom the connection
        var message = ByteBuffer.allocate(1024)
        var length = conn.read(message)
        var token = ""
        if (length >= 0){
            for (i in 0 until length){
                token += message[i].toChar()
            }
            if (token == "HELLO"){
                println("valid token")
            }else{
                //kill
            }
        }else if (suspects[conn.remoteAddress]!! > System.currentTimeMillis()){

        }else{
            //kill?
        }
    }
    fun isSuspect(key: SocketAddress): Boolean {
        return suspects[key] != null
    }
}