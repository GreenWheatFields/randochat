package com.randochat.main.endpoints

import com.randochat.main.database.AccountFormatter
import com.randochat.main.database.AccountRepository
import com.randochat.main.values.AuthCodes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
class RegisterAccount @Autowired constructor(final val accountRepo: AccountRepository) {
//    val accountFormatter: AccountFormatter = AccountFormatter(accountRepo)
    // todo, add security/tokens
    @PostMapping("/accounts/create")
    fun register(@RequestHeader("newAccount", required = true) newAccount: String,
                 @RequestHeader("code", required = true) code: String, ): ResponseEntity<Any> {


    //todo, allValid() method somewhere that contains all the code below?
        val acc = AccountFormatter.stringToAccount(AccountFormatter.decodeAccount(newAccount)) ?: return ResponseEntity("bad account", HttpStatus.BAD_REQUEST)
        if ((!AuthCodes.codes.contains(code))) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }else if(accountRepo.existsById(acc.email)){
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }else{
            accountRepo.save(acc)
            return ResponseEntity(HttpStatus.OK)
        }
    }

}

