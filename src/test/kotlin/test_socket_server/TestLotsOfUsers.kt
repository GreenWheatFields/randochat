package test_socket_server

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.lang.Thread.sleep
import kotlin.system.exitProcess

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestLotsOfUsers {
    //asuming all of byte buffer == the message
    val server = TestConnectionEvents()
    val clients = ArrayList<MultiThreadedClient>()
    @BeforeEach
    fun setup(){
        server.resetServer()
        clients.clear()
    }
    @Test
    fun testLotsOfConversatoins(){
        //todo, test latency in network and in time it takes to handle a connection?
        //tests won't be as useful until the client threads are on another computer
        repeat(250){
            clients.add(MultiThreadedClient(Client(server.port)).also { it.connectAndIntroduce() })
        }
        clients.forEach {
            it.flag = false
            it.start()
        }
//        println("threads created")
        sleep(10_000)
        clients.forEach {
            it.flag = false
            it.join()
        }
    }
}