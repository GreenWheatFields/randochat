package com.randochat.main.socket_server

import org.junit.jupiter.api.*
import java.lang.Thread.sleep
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestConversations {
    val testConnectionEvents = TestConnectionEvents()
    var clients = ArrayList<MultiThreadedClient>()
    var port = 0
    @BeforeEach
    fun setup(){
        testConnectionEvents.resetServer()
        port = testConnectionEvents.port
        clients.clear()
    }
    fun addClients(amount: Int){
        repeat(amount){
            clients.add(MultiThreadedClient(Client(port)))
        }
    }

    @Test
    fun testOneWayRelay() {
        addClients(2)
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
        assert(Arrays.equals(messages,clients[1].input.toArray()))

    }
//    @Test
    @RepeatedTest(10)
    fun testTwoWayRelay(){
        val client1Messages = ArrayList<String>()
        val client2Messages = ArrayList<String>()
        val random = Random(System.currentTimeMillis())
        addClients(2)
        clients.forEach { it.connectAndIntroduce()
            it.start()
        }
        sleep(100L)
        clients.forEach {
            it.input.clear() }
        repeat(300){
            client1Messages.add(random.nextInt().toString())
            client2Messages.add(random.nextInt().toString())
        }
        for (i in client1Messages.indices){
            clients[0].write(client1Messages[i])
            sleep(0, 1)
            clients[1].write(client2Messages[i])
        }
        sleep(10L)
        clients.forEachIndexed { index, it ->
            it.flag = false
            it.interrupt()
            it.join()
            if (it.input.size != client1Messages.size){
                //mainly fails when the initial welcome message isnt clearing from the input array
                fail("Client $index recieved ${it.input.size}. sent ${client1Messages.size}")
            }
        }
        assert(Arrays.equals(client1Messages.toArray(), clients[1].input.toArray())
                && Arrays.equals(client2Messages.toArray(), clients[0].input.toArray()))
    }
}