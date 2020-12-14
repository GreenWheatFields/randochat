package com.randochat.main.socketserver.messages

import com.randochat.main.socketserver.dataAccsess.JsonValues
import javax.json.Json

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
    }
}