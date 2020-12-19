package test_socket_server

import com.randochat.main.socketserver.messages.ClientMessages
import com.randochat.main.socketserver.serverBehavior.DirectConnections
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
    lateinit var clients: List<Client>

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
        server.start()
    }


    @Test
    fun testReconnect(){
        clients = listOf<Client>(Client(port), Client(port))
        clients.forEach { it.connect(0, true) }
        sleep(100)
        clients.forEach { it.introduce(ClientMessages.initMessage) }
//        val json = clients[0].waitForResponse()
//        val json2 = clients[1].waitForResponse()
//        println("conn")
//        val roomId = (json.get("status") as JsonObject).get("roomID").toString()
//        val userID = (json.get("status") as JsonObject).get("userID").toString()
//
//        val roomId2 = (json2.get("status") as JsonObject).get("roomID").toString()
//        val userID2= (json2.get("status") as JsonObject).get("userID").toString()
//        println(userID)
//        println(userID2)
//        clients.forEach { it.send() }
//        clients[1].send()
//        clients[0].closeConnection()
//        clients[0].connect(100, true)
//        clients[0].introduce(ClientMessages.getReconnectMessage(roomId, userID))
//        clients[0].send()
//        clients[0].closeConnection()
//        clients[1].send()
        //todo weird behavior when dc is before messages are being sent??
        //this could lead to two idle connections never having their room checked for timeout??
    }
    @Test
    fun testBadJson(){
        //bad json gets rejected
        //timeout gets rejected
        //good json but bad token gets rejected
        clients = listOf(Client(port))
        clients[0].connect(0, true)
        clients[0].introduce(Json.createObjectBuilder().add("badKey", "badValue").build().toString())
        assertThrows<IOException> { clients[0].send(1) }

    }

}

//fun main() {
//    TestConnectionEvents().testReconnect()
//}