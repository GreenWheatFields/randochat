package com.randochat.main.springapp.endpoints

import com.randochat.main.springapp.database.AccountRepository
import org.apache.catalina.connector.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GetAccount @Autowired constructor(val accountRepository: AccountRepository){
    //authorized users only. called after the server gives a client an account id. acsess to this endpoint needs to be strict
    //maybe will need another accountProperties table so I can't accidentally return an accounts password.
    //check if
    @GetMapping("/accounts/get")
    fun findAccount(@RequestHeader("account") account: String,
                    @RequestHeader("token", required = true) token: String): ResponseEntity<Any>{
//        if ()
        if (accountRepository.existsById(account)){
            //if isAuthorized
            //return the account details
        }else{
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }
            return ResponseEntity(HttpStatus.OK)
    }
}