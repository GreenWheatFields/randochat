package com.randochat.main.endpoints

import com.randochat.main.database.AccountRepository
import org.apache.catalina.connector.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GetAccount @Autowired constructor(val accountRepository: AccountRepository){
    //authorized users only. called after the server gives a client an account id. acsess to this endpoint needs to be strict
    //maybe will need another accountProperties table so I can't accidentally return an accounts password.

    @GetMapping("/accounts/get")
    fun findAccount(@RequestParam("account") account: String): ResponseEntity<Any>{
        if (accountRepository.existsById(account)){
            //if isAuthorized
            //return the account
        }else{
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }
            return ResponseEntity(HttpStatus.OK)
    }
}