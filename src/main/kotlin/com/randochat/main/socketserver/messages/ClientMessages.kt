package com.randochat.main.socketserver.messages

import com.randochat.main.socketserver.dataAccsess.JsonValues
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.*
import javax.json.*

class ClientMessages {
    companion object{
        //todo token
        val initMessage = Json.createObjectBuilder().add("intent", "OPENNEW")
                .add("token", "VALIDTOKEN").build().toString()
        fun getReconnectMessage(roomId: String, userID: String): String{
            return Json.createObjectBuilder().add("intent", "RECONNECT").add("token", "VALIDTOKEN")
                    .add("roomID", JsonValues.strip(roomId))
                    .add("userID", JsonValues.strip(userID))
                    .build().toString()
        }
        //todo will worry about structuring content messages later. this might not be needed.
//        fun getContentMessage(content: ByteBuffer): ByteBuffer {
//            return Messages.messageToBuffer(Json.createObjectBuilder()
//                    .add("content", String(Base64.getEncoder().encode(content.array()))).build())
//        }

    }
}