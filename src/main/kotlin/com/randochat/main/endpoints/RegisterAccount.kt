package com.randochat.main.endpoints

import com.randochat.main.database.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RegisterAccount{
    val accountService = AccountService()

    @RequestMapping("/accounts/create")
    fun register(){
        //get headers and create and account object, check it's validity, also check wheter or not the request is coming from a mobile
        //userAgent and contains the key in the app.
        accountService.test1()
    }

}