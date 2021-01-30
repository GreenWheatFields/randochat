package com.randochat.main.socket_server.dataAccsess

import com.auth0.jwt.interfaces.DecodedJWT
import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.util.*

//all connections are assignned to a user object.
//Rooms contain multiple users.
class User (var socketChannel: SocketChannel){
    var room: Room? = null
    var pair: User? = null
    var token: DecodedJWT? = null
    var address: SocketAddress = socketChannel.remoteAddress
    var isAuthorized: Boolean = false
    var authTimeOut: Long = System.currentTimeMillis() + 1000
    var userId: String = UUID.randomUUID().toString()
    var internalId: UUID = UUID.randomUUID()
    var blockList: Array<String>? = null
    var ping: Int = 0
    var actualUserId: String? = null

    fun reassign(user: User){
        socketChannel = user.socketChannel
        address = user.socketChannel.remoteAddress
    }
    fun assignTokenValues(token: DecodedJWT){
        //dont think this will work rn
        blockList = token.getClaim("blockList").asArray(String::class.java)
        ping = token.getClaim("ping").asInt()
        actualUserId = token.getClaim("id").asString()
    }
}