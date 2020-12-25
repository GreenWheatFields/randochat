package com.randochat.main.socket_server.dataAccsess

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
    var internalId: UUID = UUID.randomUUID()

    fun reassign(user: User){
        socketChannel = user.socketChannel
        address = user.socketChannel.remoteAddress
    }

}