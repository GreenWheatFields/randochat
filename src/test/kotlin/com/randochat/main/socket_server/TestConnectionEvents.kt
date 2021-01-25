package com.randochat.main.socket_server

import com.randochat.main.socket_server.messages.ClientMessages
import com.randochat.main.socket_server.serverBehavior.DirectConnections
import org.junit.jupiter.api.*
import java.io.IOException
import java.lang.Thread.sleep
import javax.json.Json
import javax.json.JsonObject

//todo, junit
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestConnectionEvents{
    var port = 15620
    var server = DirectConnections(port)
    lateinit var client: Client
    lateinit var clients: ArrayList<Client>

    @BeforeEach
    fun resetServer(){
        port++
        server.flag = false
        server.interrupt()
        server.join()
        server = DirectConnections(port)
        if (!server.bind()){
            port++
            resetServer()
        }
        client = Client(port)
        clients = ArrayList<Client>()
        server.start()
    }


    @Test
    fun testOneReconnect(){
        clients.add(Client(port))
        clients.add(Client(port))
        clients.forEach { it.connect(0, true) }
        sleep(100)
        clients.forEach { it.introduce(ClientMessages.initMessage) }
        val json = clients[0].waitForResponse()
        val json2 = clients[1].waitForResponse()
        val roomId = (json.get("status") as JsonObject).get("roomID").toString()
        val userID = (json.get("status") as JsonObject).get("userID").toString()
        val room = server.directory.getRoom(roomId)

        clients.forEach { it.send(20) }
        clients[0].closeConnection()
        clients[1].send(5)
        if (room.isHealthy) fail("disconnect not detected")
        clients[0].connect(0, true)
        clients[0].introduce(ClientMessages.getReconnectMessage(roomId, userID))
        clients[0].send(20)
        assert(room.isHealthy)
    }
    fun testIdleConnInRoom(): Nothing = TODO()
    //todo weird behavior when dc is before messages are being sent??
    //this could lead to two idle connections never having their room checked for timeout??
    @Test
    fun testIdleConnections(){
        for (i in 0..1000){
            clients.add(Client(port))
        }
        clients.forEach { it.connect(0, true) }
        sleep(11_000)
        clients[0].introduce(ClientMessages.initMessage)
        sleep(500L)
        //todo, optionally remove sleep from client.sleep methos
        clients.forEach { assertThrows<IOException>{ it.send(1, false) } }
    }
    @Test
    fun testBadJson(){
        //timeout gets rejected
        client.connect(0, true)
        client.introduce(Json.createObjectBuilder().add("badKey", "badValue").build().toString())
        assertThrows<IOException> { client.send(1) }
    }
    @Test
    fun testNoJson(){
        //only fails if client.send(2)
        client.connect(0 , true)
        client.introduce("noJson")
        assertThrows<IOException> {client.send(2)}

    }
    @Test
    fun testTimeout(){
        client.connect(0 , true)
        sleep(1100L)
        client.introduce(ClientMessages.initMessage)
        assertThrows<IOException> {client.send(2)  }
    }

}
