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


@SpringBootTest
@AutoConfigureMockMvc
class TestLoginLogout {

    @Test
    fun testRegisterEndpoint(@Autowired mockServer: MockMvc) {
        mockServer.perform(get("/accounts/create", )
                .header("newAccount", AccountAccessor.encodeAccount("email@email.com-username-password")))
                .andExpect(status().isOk)
                .andExpect(content().string("registered"))
    }
    @Test
    fun testEncodeAndDecode(){
        val account = "email@email.com%username%password"
        val final = AccountAccessor.decodeAccount(AccountAccessor.encodeAccount(account))
        assert(account == final)
    }
    @Test
    fun testExtractAccountfromString(){
        val accountString = "email@email.com%username%password"
        val account = Account()
        account.email = "email@email.com"
        account.password = "password"
        account.userName = "username"
        val accountToCompare = AccountAccessor.stringToAccount(accountString)

    }

}