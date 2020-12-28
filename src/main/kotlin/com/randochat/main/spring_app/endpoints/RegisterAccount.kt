package com.randochat.main.spring_app.endpoints

import com.randochat.main.spring_app.utility.AccountFormatter
import com.randochat.main.spring_app.database.AccountRepository
import com.randochat.main.spring_app.values.AuthCodes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class RegisterAccount @Autowired constructor(final val accountRepo: AccountRepository) {
//    val accountFormatter: AccountFormatter = AccountFormatter(accountRepo)
    // todo, add security/tokens
    @PostMapping("/accounts/create")
    fun register(@RequestHeader("newAccount", required = true) newAccount: String,
                 @RequestHeader("code", required = true) code: String, ): ResponseEntity<Any> {

        val acc = AccountFormatter.b64StringToAccount(AccountFormatter.decodeAccount(newAccount)) ?: return ResponseEntity("bad account", HttpStatus.BAD_REQUEST)
        if (!AuthCodes.codes.contains(code)) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }else if(accountRepo.existsById(acc.email)){
            return ResponseEntity("email already exist" ,HttpStatus.UNAUTHORIZED)
        }else{
            accountRepo.save(acc)
            return ResponseEntity(HttpStatus.OK)
        }
    }

}

