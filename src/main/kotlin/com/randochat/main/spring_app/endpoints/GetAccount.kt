package com.randochat.main.spring_app.endpoints

import com.randochat.main.spring_app.database.AccountRepository
import com.randochat.main.spring_app.utility.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class GetAccount @Autowired constructor(val accountRepository: AccountRepository){
    //authorized users only. called after the server gives a client an account id. acsess to this endpoint needs to be strict
    //maybe will need another accountProperties table so I can't accidentally return an accounts password.
    //check if
    @GetMapping("/accounts/get")
    fun findAccount(@RequestParam("account") accountID: String,
                    @RequestParam("token", required = true,) token: String
                    ): ResponseEntity<Any>{
        val userToken = Token().checkTokenValid(token) ?: return ResponseEntity(HttpStatus.FORBIDDEN)
        if (userToken.getClaim("id").asString() == accountID){
            val acc = accountRepository.findByAccountID(accountID)!!.getProtectedAccountData()
            return ResponseEntity(acc, HttpStatus.OK)
        }
        if (accountRepository.existsById(accountID)){
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