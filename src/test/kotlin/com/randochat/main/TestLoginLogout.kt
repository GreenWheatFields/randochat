package com.randochat.main

import com.randochat.main.database.Account
import com.randochat.main.database.AccountAccessor
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.apache.commons.validator.routines.EmailValidator



@SpringBootTest
@AutoConfigureMockMvc
class TestLoginLogoutServer {

    @Test
    fun testRegisterEndpoint(@Autowired mockServer: MockMvc) {
        mockServer.perform(get("/accounts/create", )
                .header("newAccount", AccountAccessor.encodeAccount("email@email.com-username-password")))
                .andExpect(status().isOk)
                .andExpect(content().string("registered"))
    }


}

class TestLoginLogoutMethods {
    @Test
    fun testEncodeAndDecode(){
        val account = "email@email.com%username%password"
        val final = AccountAccessor.decodeAccount(AccountAccessor.encodeAccount(account))
        assert(account == final)
    }
    @Test
    fun testExtractAccountfromString(){
        val accountString = "email@email.com\\username\\password"
        val account = Account()
        account.email = "email@email.com"
        account.password = "password"
        account.userName = "username"
        val accountToCompare = AccountAccessor.stringToAccount(accountString)

    }
    @Test
    fun testUserNameValidator(){
        val badUserNames = mutableListOf("lol", "123", "$%%%", "abfhgnsjgurognsjfksp", "a", "&uuuu!!fsdf!")
        for (i in badUserNames){
            println(i)
            assert(!AccountAccessor.validUserName(i))
        }
        val goodUserNames = mutableListOf("1234", "2342jkkj", "%%yolo", "LEADER", "!!KJFSKGJND!")
        for (i in goodUserNames){
            println(i)
            assert(AccountAccessor.validUserName(i))
        }

    }

}
