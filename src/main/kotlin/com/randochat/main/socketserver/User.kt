package com.randochat.main.socketserver

import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.util.*

//all connections are assignned to a user object.
//Rooms contain multiple users.
class User (val socketChannel: SocketChannel){
    var room = null
    var pair = null
    var address: SocketAddress = socketChannel.remoteAddress
    var isAuthorized: Boolean = false
    var authTimeOut: Long = System.currentTimeMillis() + 1000
    val userId: UUID = UUID.randomUUID()


}