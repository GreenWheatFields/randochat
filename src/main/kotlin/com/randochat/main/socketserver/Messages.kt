package com.randochat.main.socketserver

import java.nio.ByteBuffer

class Messages {
    //these will eventually be json objects
    companion object{
        fun welcomeMessage(room: Room): ByteBuffer{
            var message = "WELCOME "
            message += room.id
            return ByteBuffer.wrap(message.toByteArray())
        }
    }
}