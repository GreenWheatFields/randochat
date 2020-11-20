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
            //staing example email@email.com\username\password\

            var start = 0
            var extractedValues = 0
            val extractedAccount = Account()
            val sb = StringBuilder()

            for (i in temp.indices){
                if (temp[i] == '\\'){
                    sb.clear()
                    for (j in start until i){
                        sb.append(temp[j])
                    }
                    //todo, this can be simplified, drys
                    when(extractedValues){
                        0 -> {
                          if (EmailValidator.getInstance().isValid(sb.toString())){
                              extractedAccount.email = sb.toString()
                              extractedValues++
                          }else{
                              //todo, handle invalid email. maybe return a null object for the caller to handle?
                          }
                        }
                        1 -> {
                            if (validUserName(sb.toString())){
                                extractedAccount.userName = sb.toString()
                            }else{

                            }
                        }
                        2 ->{
                            //todo, validate password
                            break
                        }
                    }
                    start = i + 1
                }
            }
            //todo, validate email adresses username passwords before defining account object varibles
            return Account()
        }
        fun validUserName(user: String): Boolean{
            //min of four characters, , max size of 16, for now, i'll allow three special characters"
            //todo, no swears?
            if (user.length >= 4 && user.length <= 16){
                val temp = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toHashSet()
                val temp2 = "-!#\\\$%&'*+/=?^_`{|}~."
                var counter = 0
                for (i in user){
                    if (!temp.contains(i)){
                        if(temp2.contains(i)){
                            counter++
                        }else{
                            return false
                        }
                    }
                }
                return counter <= 3
            }
            return false
        }

    }
    //todo, put all methods that help validate and acsess data in here

    fun correctPassword(): Nothing = TODO()

}