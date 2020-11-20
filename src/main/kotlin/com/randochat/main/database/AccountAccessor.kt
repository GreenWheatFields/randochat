package com.randochat.main.database

import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.collections.HashSet
import org.apache.commons.validator.routines.EmailValidator
class AccountAccessor (val accountRepository: AccountRepository){
    companion object {
        fun encodeAccount(string: String): String{
            return Base64.getEncoder().encodeToString(string.toByteArray())
        }
        fun decodeAccount(accountString: String): String {
            return String(Base64.getDecoder().decode(accountString))
        }
        //this method might be better off outside comapnion object
        fun stringToAccount(account: String): Account{
            val temp = account.toCharArray()
            // escape = "\" todo, comments in email addresses will break this
            //staing example email@email.com\username\password\

            var start = 0
            var extractedValues = 0
            val extractedAccount = Account()
            val sb = StringBuilder()

            for (i in temp.indices){
                if (temp[i] == '\\'){
                    for (j in start until i){
                        sb.append(temp[j])
                    }
                    //todo, this can be simplified, drys
                    /*when(extractedValues){
                        0 -> {
                          if (validEmail(sb.toString())){
                              extractedAccount.email = sb.toString()
                          }else{
                              //throw NullPointerException()
                              //todo, handle invalid email
                          }
                        }
                        1 -> {
                            //if validUserName
                        }
                    }*/
                    start = i + 1
                }
            }
            //todo, validate email adresses username passwords before defining account object varibles
            return Account()
        }
        fun validEmail(email: String): Boolean{
            var localFound = false
            var start = 0
            val validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-!#\$%&'*+/=?^_`{|}~.".toHashSet()
            val validDomainChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-".toHashSet()
            for (i in 0 until email.length + 1){
                if (i == email.length){
                    if (email[start] == '-' || email[i - 1] == '-'){
                        return false
                    }
                    for (j in start until i){
                        if (!validDomainChars.contains(email[j]))
                            return false
                    }
                    return true
                }
                if (email[i] == '@'){
                    for(j in start until i){
                        if (!validChars.contains(email[j])){
                            return false
                        }else if (email[j] == '.'){
                            if (j == 0 || j == i - 1){
                                return false
                            }else if (email[j - 1] == '.' || email[j + 1] == '.'){
                                return false
                            }
                        }
                        localFound = true
                        start = i + 1
                    }
                }else if (email[i] == '.' && localFound || i == email.length){
                    if (email[start] == '-' || email[i - 1] == '-'){
                        return false
                    }else if (i != email.length){
                        for (j in start until i){
                            if (!validDomainChars.contains(email[j])){
                                return false
                            }
                        }
                        start = i + 1
                    }
                }
            }
            return true
        }

    }
    //todo, put all methods that help validate and acsess data in here

    fun correctPassword(): Nothing = TODO()

}