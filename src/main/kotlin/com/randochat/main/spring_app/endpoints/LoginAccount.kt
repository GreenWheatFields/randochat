package com.randochat.main.spring_app.endpoints

import com.fasterxml.jackson.databind.JsonNode
import com.randochat.main.spring_app.database.AccountFormatter
import com.randochat.main.spring_app.database.AccountRepository
import com.randochat.main.spring_app.utility.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
class LoginAccount @Autowired constructor(final val accountRepo: AccountRepository) {
    @PostMapping("/accounts/login")
    // todo, invalid loing attempts shuold be logged somewhere
    //also maybe update status of account in database to be logged in?
    fun login(@RequestBody body: JsonNode): ResponseEntity<Any>{
        if (!body.has("account") || !body.has("code")){
            println("bad body")
        }
        val acc = AccountFormatter.stringToAccount(AccountFormatter.decodeAccount(body["account"].asText()), hashPass = false)
                ?: return ResponseEntity(HttpStatus.FORBIDDEN)
        val storedAccount = accountRepo.findByEmail(acc.email) ?: return ResponseEntity(mapOf("reason" to "email"),HttpStatus.FORBIDDEN)
        if (BCrypt.checkpw(acc.password, storedAccount.password)){
            val token = Token().genAccountToken(storedAccount)
            return ResponseEntity(mapOf("token" to token), HttpStatus.OK)
        }else{
            //todo, maybe a loginAttempt entity to store the attempt, and total nunmber of attempets?
            return ResponseEntity(mapOf("reason" to "password"), HttpStatus.UNAUTHORIZED)
        }
    }
}