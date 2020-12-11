package com.randochat.main.socketserver.messages

import javax.json.Json

class ClientMessages {
    companion object{
        val welcomeMessage = Json.createObjectBuilder().add("intent", "OPENNEW").build()
    }
}