package test_socket_server

import com.randochat.main.socketserver.messages.ClientMessages
import com.randochat.main.socketserver.serverBehavior.DirectConnections
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestConversations {

    @Test
    fun testConversation() {
//        //if another test like this is written, make a new class. not really a connection event
//        val server = DirectConnections(15620)
//        val client = MultiThreadedClient(Client(15620))
//        val client2 = MultiThreadedClient(Client(15620))

    }
}