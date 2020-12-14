package com.randochat.main.socketserver.messages

import com.randochat.main.socketserver.dataAccsess.Room
import com.randochat.main.socketserver.dataAccsess.User
import java.nio.ByteBuffer
import javax.json.Json

class ServerMessages {
    companion object{
        fun welcomeMessage(room: Room, user: User): ByteBuffer {
            val status =  Json.createObjectBuilder().add("roomID", room.id).add("roomStatus", room.isHealthy)
                    .add("otherStatus", room.isHealthy).add("userID", user.userId).build()
            val temp = Json.createObjectBuilder().add("intent", "WELCOME").add("status", status)
                    .add("content", "null").build()
            return Messages.messageToBuffer(temp)
        }
    }
}