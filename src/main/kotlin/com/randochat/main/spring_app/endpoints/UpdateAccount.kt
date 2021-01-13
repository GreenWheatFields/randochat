package com.randochat.main.spring_app.endpoints

import com.fasterxml.jackson.databind.JsonNode
import com.randochat.main.spring_app.database.AccountRepository
import com.randochat.main.spring_app.utility.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateAccount @Autowired constructor(final val accountRepo: AccountRepository){
    //todo check bio length
    //verify, image
    //name/email/password/ changes
    @PostMapping("/accounts/update")
    fun update(@RequestBody body: JsonNode): ResponseEntity<Any>{
        //parse json, for intry in json uupdate.validate in another method
        //check token and account etc
        if (!body.contains("token")){
            return ResponseEntity("no token", HttpStatus.UNAUTHORIZED)
        }

        val token = Token().checkTokenValid(body["token"].toString()) ?: return ResponseEntity("bad token", HttpStatus.UNAUTHORIZED)
        val account = accountRepo.findByAccountID(token.id) ?: ResponseEntity("acciybt not found", HttpStatus.FORBIDDEN)
        var jsonSize = 0
        for (i in body.fieldNames()){
            if (jsonSize > 20){
                return ResponseEntity("too many entries",HttpStatus.FORBIDDEN)
            }
            when (i){
                "bio" -> updateBio()
            }
            jsonSize++

        }
        return ResponseEntity(HttpStatus.OK)
    }
    fun updateBio(): Nothing = TODO()
}