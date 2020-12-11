package com.randochat.main.socketserver.messages

import javax.json.Json

class ClientMessages {
    companion object{
        //todo token
        val initMessage = Json.createObjectBuilder().add("intent", "OPENNEW")
                .add("token", "VALIDTOKEN").build().toString()
    }
}