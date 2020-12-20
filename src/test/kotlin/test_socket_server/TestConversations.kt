package test_socket_server

import com.randochat.main.socketserver.messages.ClientMessages
import com.randochat.main.socketserver.serverBehavior.DirectConnections
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.lang.Thread.sleep
import java.util.*
import kotlin.collections.ArrayList

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestConversations {
    val testConnectionEvents = TestConnectionEvents()
    var clients = ArrayList<MultiThreadedClient>()
    var port = 0
    @BeforeEach
    fun setup(){
        testConnectionEvents.resetServer()
        port = testConnectionEvents.port
    }

    @Test
    fun testConversation() {
        clients.add(MultiThreadedClient(Client(port)))
        clients.add(MultiThreadedClient(Client(port)))
        clients.forEach { it.connectAndIntroduce()
            it.start()
            sleep(50L)
            it.input.clear()
        }

        val messages = arrayOf("one", "two", "three", "four", "five", "six")
        for (i in messages){
            clients[0].write(i)
            sleep(100L)
        }
        clients.forEach { it.flag = false
            it.interrupt()
            it.join()}
        assert(Arrays.equals(messages,clients[1].input.toArray() ))

    }
}