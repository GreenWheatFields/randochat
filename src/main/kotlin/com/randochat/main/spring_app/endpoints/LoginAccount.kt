package com.randochat.main.spring_app.endpoints

import com.fasterxml.jackson.databind.JsonNode
import com.randochat.main.spring_app.database.AccountFormatter
import com.randochat.main.spring_app.database.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
class LoginAccount @Autowired constructor(final val accountRepo: AccountRepository) {
    @PostMapping("/accounts/login")
    //check for correct credent
    fun login(@RequestBody body: JsonNode): ResponseEntity<Any>{
        if (!body.has("account") || !body.has("code")){
            println("bad body")
        }
        val acc = AccountFormatter.stringToAccount(body["account"].asText(), false)
        println(acc == null)
//        if(!AccountFormatter.validRequestParamsAcc(request, "account", "code")){
//            return ResponseEntity(HttpStatus.BAD_REQUEST)
//        }
//        val acc = AccountFormatter.stringToAccount(account, false)
////        val i = charArrayOf('a','b','c','d')
//        //todo, invalid loing attempts shuold be logged somewhere
//        val toCompare = accountRepo.findByEmail(acc!!.email) ?: return ResponseEntity(HttpStatus.OK)
//        if (BCrypt.checkpw(acc.password, toCompare.password)){
//            //logged in, do something
//            //should pacakage the profile info text and links to the profiles images here?
////                return ResponseEntity(headers = "gsdf")
//            ;
//        }else{
//            //invalid password, do something;
//            //todo, maybe a loginAttempt entity to store the attempt, and total nunmber of attempets?
//            ;
//            return(ResponseEntity(HttpStatus.UNAUTHORIZED))
//        }
//

        return ResponseEntity(HttpStatus.OK)
    }
}