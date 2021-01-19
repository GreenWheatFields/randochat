package com.randochat.main.spring_app

import com.fasterxml.jackson.databind.ObjectMapper
import com.randochat.main.spring_app.utility.AccountFormatter
import com.randochat.main.spring_app.values.TestAccounts
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.client.match.MockRestRequestMatchers.content
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.json.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.system.exitProcess

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestAccountFlow {
    lateinit var accessToken: String
    lateinit var accountId: String
    @BeforeAll
    fun login(@Autowired mockServer: MockMvc){
        val json = HashMap<String, String>()
        json.put("account", AccountFormatter.encodeAccountString((TestAccounts.testAccount1.email + "\\" + TestAccounts.testAccount1.password + "\\")))
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
    fun testGetAccount(@Autowired mockServer: MockMvc){
        mockServer.perform(get("/accounts/get?account=$accountId&token=$accessToken"))
                .andExpect(status().isOk)
        //todo, no content
    }
    @Test
    fun testUpdateAccBio(@Autowired mockServer: MockMvc){
        println(accessToken)
    }

}