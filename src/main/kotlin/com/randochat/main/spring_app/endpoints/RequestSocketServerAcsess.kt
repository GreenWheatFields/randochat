package com.randochat.main.spring_app.endpoints

import com.fasterxml.jackson.databind.JsonNode
import com.randochat.main.spring_app.utility.Token
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.system.exitProcess

//query socket server and see if it wants another connection
//later this could be used as a type of load balancer

//for now, create a token containing all data needed for the socket server to accept a conn
// and create a pait for it
@RestController
class RequestSocketServerAcsess {
    @PostMapping("/accounts/talk")
    fun genSocketToken(@RequestBody body: JsonNode, request: HttpServletRequest): ResponseEntity<Any> {
        //take in the token returned on login. add ip addrses and ping.
        if (!body.has("token") || !body.has("time") || !body.has("ping")){
            //bad request
        }
        val token = Token().checkTokenValid(body["token"].toString().substring(1, body["token"].toString().length - 1)) ?: exitProcess(1)

        val newToken = Token().copyToken(token) ?: return ResponseEntity<Any>(HttpStatus.INTERNAL_SERVER_ERROR)
        val ping = System.currentTimeMillis() - body["time"].asLong()
        newToken.withExpiresAt(Date(System.currentTimeMillis() + 100_000))
        newToken.withClaim("ping", ping)
        newToken.withClaim("ipAddress", request.remoteAddr)
        newToken.withClaim("serverKey", "serverKEY") // servev gens a randome key every so often
        //eventually send ip address of socket server
        return ResponseEntity<Any>(mapOf("socketToken" to Token().sign(newToken)), HttpStatus.OK)


    }
}