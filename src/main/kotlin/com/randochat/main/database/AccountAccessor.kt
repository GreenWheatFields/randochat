package com.randochat.main.database

import org.apache.commons.validator.routines.EmailValidator
import java.util.*

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
                              //invalid formats should be handled client side anyway
                          }
                        }
                        1 -> {
                            if (validUserName(sb.toString())){
                                extractedAccount.userName = sb.toString()
                            }else{

                            }
                        }
                        2 ->{
                            if (validPassword(sb.toString().toCharArray())){
                                val password = sb.toString().toCharArray()
                                sb.clear()
                                //hash password

                            }else{

                            }
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
                val temp2 = "-!#\\\$%&'*+/=?^_`{|}~.".toHashSet()
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
        fun validPassword(password: CharArray): Boolean{
            // 8 - 26 chars with symbols. needs one capital letter and one symbol
            //todo, keep reusing this. replace with enum?
            val temp = "abcdefghijklmnopqrstuvwxyz0123456789".toHashSet()
            val temp2 = "-!#\\\$%&'*+/=?^_`{|}~.".toHashSet()
            val temp3 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toHashSet()
            var symbol = 0
            var capital = 1
            for (i in password.indices){
                if (password.size < 8 || password.size > 26){
                    break
                }
                if (!temp.contains(password[i])){
                    if (temp2.contains(password[i])){
                        symbol = 2
                    }else if (temp3.contains(password[i])){
                        capital = 2
                    }
                }

            }
            for (i in password.indices){
                password[i] = 0.toChar()
            }
            return symbol == capital
        }

    }

    fun correctPassword(): Nothing = TODO()

}