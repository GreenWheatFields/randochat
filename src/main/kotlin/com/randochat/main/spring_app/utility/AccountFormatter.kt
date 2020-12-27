package com.randochat.main.spring_app.utility

import com.randochat.main.spring_app.database.Account
import com.randochat.main.spring_app.database.AccountRepository
import com.randochat.main.spring_app.values.AuthCodes
import org.apache.commons.validator.routines.EmailValidator
import java.util.*
import org.springframework.security.crypto.bcrypt.BCrypt
import java.io.StringReader
import javax.json.Json
import javax.json.JsonObject
import javax.servlet.http.HttpServletRequest

class AccountFormatter (val accountRepository: AccountRepository){
    //formats the way accountstrings are transferred between client and server
    companion object {
        fun encodeAccountString(string: String): String{
            //todo exception handling here?
            return Base64.getEncoder().encodeToString(string.toByteArray())
        }
        fun encodeAccountObject(acc: Account): String{
            return acc.email + "\\" + acc.password
        }
        fun decodeAccount(accountString: String): String {
            return String(Base64.getDecoder().decode(accountString))
        }
        //this method might be better off outside comapnion object
        fun n64StringToAccount(account: String, hashPass: Boolean = true, newAccount: Boolean = false): Account?{
            var temp = account.toCharArray()
            //todo, accept either email or username
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
                    when(extractedValues){
                        0 -> {
                          if (EmailValidator.getInstance().isValid(sb.toString())){
                              extractedAccount.email = sb.toString()
                              extractedValues++
                          }else{
                              extractedValues = 5
                              break
                          }
                        }
                        1 -> {
                            if (validUserName(sb.toString())){
                                extractedAccount.userName = sb.toString()
                                extractedValues++
                            }
                        }
                        2 ->{
                            if (validPassword(sb.toString().toCharArray())){
                                val password = if (hashPass) BCrypt.hashpw(sb.toString(), BCrypt.gensalt()) else (sb.toString())
                                sb.clear()
                                extractedAccount.password = password
                                extractedValues++
                            }
                            break
                        }
                    }
                    start = i + 1
                }
            }
            if (newAccount){
                extractedAccount.accountID = UUID.randomUUID().toString()
                extractedAccount.accountStatus = "good"
            }
            return if (extractedValues == 5 || extractedValues < 3) null else (extractedAccount)
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
            //todo, keep reusing this. replace with constant somewhere?
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

//        fun validRequestParamsAcc(request: HttpServletRequest, accKey: String, codeKey: String): Boolean{
//            //todo, might break at a null request object
//            //todo, this shuold ideally return an account object. as the current way calls some of these methods twice
//            return(stringToAccount(decodeAccount(request.getHeader(accKey))) != null && AuthCodes.codes.contains(request.getHeader(codeKey)))
//        }


    }

}