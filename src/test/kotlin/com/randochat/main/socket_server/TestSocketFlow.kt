package com.randochat.main.socket_server

import com.auth0.jwt.JWTCreator
import com.randochat.main.socket_server.serverBehavior.DirectConnections
import com.randochat.main.spring_app.database.Account
import com.randochat.main.spring_app.utility.Token
import com.randochat.main.spring_app.values.TestAccounts
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import kotlin.system.exitProcess


//mockmvc
//get token for two clients
//start socket server
//connect to socket server
//return a pair
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestSocketFlow {
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
        print("starting server")
        val server = DirectConnections(15620)
        server.start()
    }
    @Test
    fun testSocketS(){
        var token1 = login(account1)
        while (true){
            ;
        }
    }

}