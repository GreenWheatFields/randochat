package com.randochat.main.spring_app

import com.fasterxml.jackson.databind.ObjectMapper
import com.randochat.main.spring_app.database.Account
import com.randochat.main.spring_app.database.AccountRepository
import com.randochat.main.spring_app.utility.AccountFormatter
import com.randochat.main.spring_app.values.TestAccounts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.client.match.MockRestRequestMatchers.content
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.json.*
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.system.exitProcess
import com.randochat.main.spring_app.values.TestAccounts.Companion.testAccount1
import org.junit.jupiter.api.*
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class TestAccountFlow {
    lateinit var accessToken: String
    lateinit var accountId: String
    @Autowired
    lateinit var accountRepo: AccountRepository
    @BeforeAll
    fun login(@Autowired mockServer: MockMvc){
        val json = HashMap<String, String>()
        json.put("account", AccountFormatter.encodeAccountString((testAccount1.email + "\\" + testAccount1.password + "\\")))
        json.put("code", "code")
        mockServer.perform(MockMvcRequestBuilders.post("/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(json)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty)
                .andDo {
                    try {
                        JSONObject(it.response.contentAsString).also {
                            accessToken = it["token"].toString()
                            accountId = it["id"].toString()
                        }

                    }
                    catch (e: JSONException){
                        println("bad token or bad json parse")
                        exitProcess(1)
                    }
                     }

    }
    @Test
    fun testGetOwnAccount(@Autowired mockServer: MockMvc){
        mockServer.perform(get("/accounts/get?account=$accountId&token=$accessToken"))
                .andExpect(status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(accountId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bio").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist())
    }
    @Test
    fun testGetSocketKey(@Autowired mockServer: MockMvc){
        //todo token error
        mockServer.perform(MockMvcRequestBuilders.post("/accounts/talk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(
                        mapOf(
                                "token" to accessToken,
                                "time" to System.currentTimeMillis())
                )))
                .andExpect(status().isOk)
    }

    @Test
    @Transactional
    fun testUpdateAccBio(@Autowired mockServer: MockMvc){
        val new = accountRepo.findByAccountID(testAccount1.accountID)?.email ?: fail("account not found")
        mockServer.perform(MockMvcRequestBuilders.post("/accounts/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(
                        mapOf(
                                "token" to accessToken,
                                "email" to "newemail@email.com"
                )))).andExpect(status().isOk)
        assert(accountRepo.findByAccountID(testAccount1.accountID)!!.email != new &&accountRepo.findByAccountID(testAccount1.accountID)!!.email == "newemail@email.com")
    }

}