package com.randochat.main.socketserver.tests

import com.randochat.main.socketserver.messages.ClientMessages
import com.randochat.main.socketserver.serverBehavior.DirectConnections
import java.lang.Thread.sleep
import javax.json.JsonObject

class TestConnectionEvents{
    val server = DirectConnections()
    val clients = listOf<Client>(Client(), Client())
    fun testReconnect(){
        server.start()
        clients.forEach { it.connect(0, true) }
        sleep(100)
        clients.forEach { it.introduce(ClientMessages.initMessage) }
        val json = clients[0].waitForResponse()
        val roomId = (json.get("status") as JsonObject).get("roomID").toString()
//        clients.forEach { it.send() }
        clients[0].closeConnection()
        clients[1].send()
        println(roomId)
        clients[0].connect(100, true)
        clients[0].introduce(ClientMessages.getReconnectMessage(roomId))
        //todo weird behavior when dc is before messages are being sent??
        //this could lead to two idle connections never having their room checked for timeout??
    }
}

fun main() {
    TestConnectionEvents().testReconnect()
}