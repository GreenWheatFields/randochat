package com.randochat.main.socketserver.dataAccsess

import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.util.*

//all connections are assignned to a user object.
//Rooms contain multiple users.
class User (var socketChannel: SocketChannel){
    var room: Room? = null
    var pair: User? = null
    var address: SocketAddress = socketChannel.remoteAddress
    var isAuthorized: Boolean = false
    var authTimeOut: Long = System.currentTimeMillis() + 1000
    var userId: String = UUID.randomUUID().toString()

    fun reassign(channel: SocketChannel){
        socketChannel = channel
        address = socketChannel.remoteAddress
    }

}