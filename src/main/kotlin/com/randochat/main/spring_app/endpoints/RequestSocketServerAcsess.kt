package com.randochat.main.spring_app.endpoints

import org.springframework.web.bind.annotation.RestController

//query socket server and see if it wants another connection
//later this could be used as a type of load balancer

//for now, create a token containing all data needed for the socket server to accept a conn
// and create a pait for it
@RestController
class RequestSocketServerAcsess {
}