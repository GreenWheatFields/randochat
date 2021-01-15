package com.randochat.main.spring_app.endpoints

import com.fasterxml.jackson.databind.JsonNode
import com.randochat.main.spring_app.database.Account
import com.randochat.main.spring_app.database.AccountRepository
import com.randochat.main.spring_app.utility.AccountFormatter
import com.randochat.main.spring_app.utility.Token
import org.apache.commons.validator.routines.EmailValidator
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
    lateinit var account: Account
    @PostMapping("/accounts/update")
    fun updateAccount(@RequestBody body: JsonNode): ResponseEntity<Any>{
          //if invalid bio
        //json {token, bio/username/picture/etce}
        // can only update one field per call?
        if (!body.contains("token")){
            return ResponseEntity("no token", HttpStatus.UNAUTHORIZED)
        }

        val token = Token().checkTokenValid(body["token"].toString()) ?: return ResponseEntity("bad token", HttpStatus.UNAUTHORIZED)
        account = accountRepo.findByAccountID(token.id) ?: return ResponseEntity("acciybt not found", HttpStatus.FORBIDDEN)
        var jsonSize = 0
        for (field in body.fieldNames()){
            if (jsonSize > 3){
                return ResponseEntity("too many entries",HttpStatus.FORBIDDEN)
            }
            val content = body[field].toString()
            when (field){
                "bio" -> updateBio(content)
                "email" -> updateEmail(content)
                "password" -> updatePassword(content)
                "location" -> updateLocation(content)
                "user" -> updateUsername(content)
                "image" -> updateImage(content)
            }
            jsonSize++

        }
        accountRepo.save(account)
        return ResponseEntity(HttpStatus.OK)

    }
    fun updateBio(newBio: String) {
        if (newBio.length > 150){
            account.bio = newBio
        }else{
            //return ResponseEntitiy("bio too long, Htpp.OK)
        }
    }
    fun updateEmail(newEmail: String){
        if(EmailValidator.getInstance().isValid(newEmail)){
            //todo, send a code to the new email address to see if its valid
            account.email = newEmail
        }
    }
    fun updatePassword(newPass: String){
        if (AccountFormatter.validPassword(newPass.toCharArray())){
            account.password = newPass
        }
    }
    fun updateLocation(newLocation: String){
        account.location = newLocation
    }
    fun updateUsername(newUser: String){
        if (AccountFormatter.validUserName(newUser)){
            account.userName = newUser
        }
    }
    fun updateImage(imageURL: String){
        //todo
        account.imageLink = imageURL
    }

}