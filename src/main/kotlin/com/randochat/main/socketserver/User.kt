package com.randochat.main.socketserver

import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.util.*

//all connections are assignned to a user object.
class User (val channel: SocketChannel){
    var address: SocketAddress = channel.remoteAddress
    var timeOut: Long = System.currentTimeMillis() + 1000
    val userId: UUID = UUID.randomUUID()
}