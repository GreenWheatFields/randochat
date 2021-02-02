package com.randochat.main.socket_server.messages

import com.randochat.main.socket_server.dataAccsess.JsonValues
import javax.json.*

class ClientMessages {
    companion object{
        //todo token
        fun initMessage(){
            Json.createObjectBuilder().add("intent", "OPENNEW")
            .add("token", "VALIDTOKEN").build().toString()
        }
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