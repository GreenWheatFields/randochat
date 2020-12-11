package com.randochat.main.socketserver.tests

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
        clients.forEach { it.introduce("HELLO") }
        val json = clients[0].waitForResponse()
        val roomId = (json.get("status") as JsonObject).get("roomID").toString()
        clients[0].closeConnection()
    }
}

fun main() {
    TestConnectionEvents().testReconnect()
}