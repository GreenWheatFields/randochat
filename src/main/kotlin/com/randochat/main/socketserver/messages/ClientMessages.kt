package com.randochat.main.socketserver.messages

import javax.json.Json
import javax.json.JsonObject

class ClientMessages {
    companion object{
        //todo token
        val initMessage = Json.createObjectBuilder().add("intent", "OPENNEW")
                .add("token", "VALIDTOKEN").build().toString()
        fun getReconnectMessage(roomId: String): String{
            val id = roomId.substring(1, roomId.length - 1)
            return Json.createObjectBuilder().add("intent", "RECONNECT").add("token", "VALIDTOKEN").add("roomID", id).build().toString()
        }
    }
}