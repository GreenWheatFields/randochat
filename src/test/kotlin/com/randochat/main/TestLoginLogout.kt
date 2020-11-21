package com.randochat.main

import com.randochat.main.database.Account
import com.randochat.main.database.AccountFormatter
import com.randochat.main.values.AuthCodes
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.junit.jupiter.api.fail
import org.springframework.security.crypto.bcrypt.BCrypt


@SpringBootTest
@AutoConfigureMockMvc
class TestLoginLogoutServer {

    @Test
    fun testRegisterEndpoint(@Autowired mockServer: MockMvc) {
        mockServer.perform(get("/accounts/create", )
                .header("newAccount", AccountFormatter.encodeAccount("email@email.com\\username\\VALIDPASSWORD856!\\"))
                .header("code", AuthCodes.codeAcsess[0]))
                .andExpect(status().isOk)
                .andExpect(content().string("registered"))
    }


}

class TestLoginLogoutMethods {
    @Test
    fun testEncodeAndDecode(){
        val account = "email@email.com%username%password"
        val final = AccountFormatter.decodeAccount(AccountFormatter.encodeAccount(account))
        assert(account == final)
    }
    @Test
    fun testExtractAccountfromString(){
        val accountString = "email@email.com\\username\\PASSWORD123!\\"
        val account = Account()
        account.email = "email@email.com"
        account.password = BCrypt.hashpw("password", BCrypt.gensalt())
        account.userName = "username"
        val accountToCompare = AccountFormatter.stringToAccount(accountString)

        if (accountToCompare == null){
            fail("invalid entry")
        }
        println("email")
        assert(account.email == accountToCompare.email)
        // can't compare the two password hashed so will compare length to see if hashed
        println("passwords")
        assert(account.password.length > 8 && accountToCompare.password.length > 8)
        println("username")
        assert(account.userName == accountToCompare.userName)
    }
    @Test
    fun testAccFromStringBadAccount(){
        val accountString = "email@email.com\\userName\\invalidpassword\\"
        val acc = AccountFormatter.stringToAccount(accountString)
        assert(acc == null)
    }
    @Test
    fun testUserNameValidator(){
        val badUserNames = mutableListOf("lol", "123", "$%%%", "abfhgnsjgurognsjfksp", "a", "&uuuu!!fsdf!")
        for (i in badUserNames){
            println(i)
            assert(!AccountFormatter.validUserName(i))
        }
        val goodUserNames = mutableListOf("1234", "2342jkkj", "%%yolo", "LEADER", "!!KJFSKGJND!")
        for (i in goodUserNames){
            println(i)
            assert(AccountFormatter.validUserName(i))
        }

    }
    @Test
    fun testPasswordValidatorGoodPass(){
        val goodPasswords = mutableListOf("Testing123!", "1AKJFDS!!!", "lolmAk5?", "!sodOLf6", "&&&&&&&&&&&&P")
        for (i in goodPasswords.indices){
            println(goodPasswords[i])
            assert(AccountFormatter.validPassword(goodPasswords[i].toCharArray()))
        }
    }
    @Test
    fun testPasswordValidatorBadPass(){
        val badPassowrds = mutableListOf("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "123", "#############################", "pas123", "!!!!!!!aslkfdslkmfldklskdfm")
        for (i in badPassowrds.indices){
            println(badPassowrds[i])
            assert(!AccountFormatter.validPassword(badPassowrds[i].toCharArray()))
        }
    }

}
