package test_socket_server

import com.randochat.main.socketserver.messages.ClientMessages
import com.randochat.main.socketserver.serverBehavior.DirectConnections
import org.junit.jupiter.api.*
import java.lang.Thread.sleep
import javax.json.JsonObject

//todo, junit
class TestConnectionEvents{
    lateinit var server: DirectConnections
    lateinit var clients: List<Client>



    @BeforeEach
    fun setup(){
        server = DirectConnections()
        server.start()
    }

    @Test
    fun testReconnect(){
        clients = listOf<Client>(Client(), Client())
        clients.forEach { it.connect(0, true) }
        sleep(100)
        clients.forEach { it.introduce(ClientMessages.initMessage) }
        val json = clients[0].waitForResponse()
        val json2 = clients[1].waitForResponse()
        val roomId = (json.get("status") as JsonObject).get("roomID").toString()
        val userID = (json.get("status") as JsonObject).get("userID").toString()

        val roomId2 = (json2.get("status") as JsonObject).get("roomID").toString()
        val userID2= (json2.get("status") as JsonObject).get("userID").toString()
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
    fun testBadAuth(){
        //bad json gets rejected
        //timeout gets rejected
        //good json but bad token gets rejected
        clients = listOf(Client())
    }
    @AfterEach
    fun tearDown(){
        server.testFlag = false
        server.interrupt()
        println("here")

    }
}

//fun main() {
//    TestConnectionEvents().testReconnect()
//}