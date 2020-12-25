package com.randochat.main.socket_server.messages

import com.randochat.main.socket_server.dataAccsess.Room
import com.randochat.main.socket_server.dataAccsess.User
import java.nio.ByteBuffer
import javax.json.Json

class ServerMessages {
    //todo, content message should be json or at least have some extra data like server time
    //also base64 encode everthing?
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