package com.randochat.main.socket_server

import com.randochat.main.socket_server.serverBehavior.DirectConnections
import com.randochat.main.spring_app.database.Account
import com.randochat.main.spring_app.utility.Token
import com.randochat.main.spring_app.values.TestAccounts
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.lang.Thread.currentThread
import java.lang.Thread.sleep
import kotlin.system.exitProcess


//mockmvc
//get token for two clients
//start socket server
//connect to socket server
//return a pair
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestSocketFlow {
    lateinit var server: DirectConnections
    val account1 = TestAccounts.testAccount1
    val account2 = TestAccounts.testAccount2
    var account1Token = login(account1)
    var account2Token = login(account2)

    fun login(account:Account): String {
        val firstToken = Token.genAccountToken(account)
        val decoded = Token.checkTokenValid(firstToken) ?: exitProcess(1)
        val copy = Token.copyToken(decoded) ?: exitProcess(1)
        copy.withClaim("ping", 15)
        copy.withClaim("ipAddresss", "IPADDRESS")
        copy.withClaim("serverKey", "serverKey")
        return Token.sign(copy)

    }
    @BeforeEach
    fun startSocketServer(){
        server = DirectConnections(15620)

    }
    @Test
    fun testSocketS(){
        print(currentThread().name)
        println("testing sockers")
        server.start()
        var token1 = login(account1)
        var token2 = login(account2)
        val threadedClient = AsyncClient(Client(15620))
        val threadedClient2 = AsyncClient(Client(15620))

        threadedClient.start()
        threadedClient2.start()
        threadedClient.connectAndIntroduce(token1)
        threadedClient2.connectAndIntroduce(token2)
        sleep(5000)
    }
    @AfterEach
    fun close(){
        print("closing")
        server.close()
    }


}