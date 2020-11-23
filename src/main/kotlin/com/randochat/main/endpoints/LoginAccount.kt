package com.randochat.main.endpoints

import com.randochat.main.database.AccountFormatter
import com.randochat.main.database.AccountRepository
import org.apache.catalina.connector.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
class LoginAccount @Autowired constructor(final val accountRepo: AccountRepository) {
    @GetMapping("/accounts/login")
    fun login(@RequestHeader("account", required = true) account: String,
              @RequestHeader("code") code: String, request: HttpServletRequest): ResponseEntity<Any>{
        if(!AccountFormatter.validRequestParamsAcc(request, "account", "code")){
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        val acc = AccountFormatter.stringToAccount(account, false)
        val i = charArrayOf('a','b','c','d')
        //todo, invalid loing attempts shuold be logged somewhere
        val toCompare = accountRepo.findByEmail(acc!!.email) ?: return ResponseEntity(HttpStatus.OK)
        if (BCrypt.checkpw(acc.password, toCompare.password)){
            //logged in, do something
            //should pacakage the profile info text and links to the profiles images here?
            ;
        }else{
            //invalid password, do something;
            //todo, maybe a loginAttempt entity to store the attempt, and total nunmber of attempets?
            ;
            return(ResponseEntity(HttpStatus.UNAUTHORIZED))
        }


        return ResponseEntity(HttpStatus.OK)
    }
}