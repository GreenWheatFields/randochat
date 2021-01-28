package com.randochat.main.spring_app.endpoints

import com.fasterxml.jackson.databind.ObjectMapper
import com.randochat.main.spring_app.database.AccountRepository
import com.randochat.main.spring_app.utility.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class GetAccount @Autowired constructor(val accountRepository: AccountRepository){
    //authorized users only. called after the server gives a client an account id. acsess to this endpoint needs to be strict
    //used to get own account or get another account. Can only get another account if the two are matched and not banned/blocked
    //todo, switch to post mapping/json?
    @GetMapping("/accounts/get")
    fun findAccount(@RequestParam("account") accountID: String,
                    @RequestParam("token", required = true,) token: String
                    ): ResponseEntity<Any>{
        val userToken = Token.checkTokenValid(token) ?: return ResponseEntity(HttpStatus.FORBIDDEN)
        if (userToken.getClaim("id").asString() == accountID){
            val acc = accountRepository.findByAccountID(accountID) ?: return ResponseEntity(HttpStatus.FORBIDDEN)
            return ResponseEntity(acc.getProtectedAccountData(), HttpStatus.OK)
        }
        if (accountRepository.existsById(accountID)){
            //either checking matches or matched via conversation. the only reason to acsess an account
            //maybe people will have acsess to small parts of the other account if theyre currently talking
            //if isAuthorized
            //!account.blocklist.contains
            //account.matches.contains
            //return the account details
        }else{
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }
            return ResponseEntity(HttpStatus.OK)
    }
}