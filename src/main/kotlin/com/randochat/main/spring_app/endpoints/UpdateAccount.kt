package com.randochat.main.spring_app.endpoints

import com.fasterxml.jackson.databind.JsonNode
import com.randochat.main.spring_app.database.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateAccount @Autowired constructor(final val accountRepo: AccountRepository){
    //todo check bio length
    //verify, image
    //name/email/password/ changes
    @PostMapping
    fun update(@RequestBody body: JsonNode){
        //parse json, for intry in json uupdate.validate in another method
    }
}