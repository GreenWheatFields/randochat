package com.randochat.main.spring_app

import com.fasterxml.jackson.databind.ObjectMapper
import com.randochat.main.spring_app.database.Account
import com.randochat.main.spring_app.utility.AccountFormatter
import com.randochat.main.spring_app.values.AuthCodes
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.collections.HashMap


@SpringBootTest
@AutoConfigureMockMvc
class TestLoginLogoutWithMockServer {
    @Test
    @Disabled
    fun testRegisterEndpoint(@Autowired mockServer: MockMvc) {
        mockServer.perform(get("/accounts/create", )
                .header("newAccount", AccountFormatter.encodeAccountString("email@email.com\\username\\VALIDPASSWORD856!\\"))
                .header("code", AuthCodes.codeAccess[0]))
                .andExpect(status().isOk)
                .andExpect(content().string("registered"))

    }

    //todo, @BeforeAll
    @Test
    @Disabled
    fun testGetAccount(@Autowired mockServer: MockMvc) {
//        mockServer.perform(get("/accounts/get?account=${UUID.randomUUID()}&token=${Token().genAccountToken()}"))
    }

    @Test
    fun testLogin(@Autowired mockServer: MockMvc) {
        var json = HashMap<String, String>()
        json.put("account", AccountFormatter.encodeAccountString("email@email.com\\Password123!\\"))
        json.put("code", "code")
        mockServer.perform(post("/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(json)))
    }

    class TestLoginLogoutMethods {
        @Test
        fun testEncodeAndDecode() {
            val account = "email@email.com%username%password"
            val final = AccountFormatter.decodeAccount(AccountFormatter.encodeAccountString(account))
            assert(account == final)
        }
        @Test
        fun testUsernameOnlyLogin(){
            val account = AccountFormatter.b64StringToAccount("userName\\Password123?\\")
                    ?: fail("null account object")
            assertThrows<UninitializedPropertyAccessException>{ account.email.length }

        }
        @Test
        fun testEmailOnlyLogin(){
            val account = AccountFormatter.b64StringToAccount("email@email.com\\password123")
                    ?: fail("null account")
            assertThrows<UninitializedPropertyAccessException> { account.userName }
        }

        @Test
        fun testExtractAccountfromString() {
            val accountString = "email@email.com\\username\\PASSWORD123!\\"
            val account = Account()
            account.email = "email@email.com"
            account.password = BCrypt.hashpw("password", BCrypt.gensalt())
            account.userName = "username"
            val accountToCompare = AccountFormatter.b64StringToAccount(accountString, newAccount = true) ?: fail("invalid entry")

            println("email")
            assert(account.email == accountToCompare.email)
            // can't compare the two password hashed so will compare length to see if hashed
            println("passwords")
            assert(account.password.length > 8 && accountToCompare.password.length > 8)
            println("username")
            assert(account.userName == accountToCompare.userName)
        }

        @Test
        fun testStringToAccount2() {
            val account = "email@email.com\\username\\VALIDPASSWORD856!\\"
            if (AccountFormatter.b64StringToAccount(account, newAccount = true) == null) fail("null")
        }

        @Test
        fun testAccFromStringBadAccount() {
            val accountString = "email@email.com\\userName\\invalidpassword\\"
            val acc = AccountFormatter.b64StringToAccount(accountString, newAccount = true)
            assert(acc == null)
        }

        @Test
        fun testUserNameValidator() {
            val badUserNames = mutableListOf("lol", "123", "$%%%", "abfhgnsjgurognsjfksp", "a", "&uuuu!!fsdf!")
            for (i in badUserNames) {
                println(i)
                assert(!AccountFormatter.validUserName(i))
            }
            val goodUserNames = mutableListOf("1234", "2342jkkj", "%%yolo", "LEADER", "!!KJFSKGJND!")
            for (i in goodUserNames) {
                println(i)
                assert(AccountFormatter.validUserName(i))
            }

        }

        @Test
        fun testPasswordValidatorGoodPass() {
            val goodPasswords = mutableListOf("Testing123!", "1AKJFDS!!!", "lolmAk5?", "!sodOLf6", "&&&&&&&&&&&&P")
            for (i in goodPasswords.indices) {
                println(goodPasswords[i])
                assert(AccountFormatter.validPassword(goodPasswords[i].toCharArray()))
            }
        }

        @Test
        fun testPasswordValidatorBadPass() {
            val badPassowrds = mutableListOf("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "123", "#############################", "pas123", "!!!!!!!aslkfdslkmfldklskdfm")
            for (i in badPassowrds.indices) {
                println(badPassowrds[i])
                assert(!AccountFormatter.validPassword(badPassowrds[i].toCharArray()))
            }
        }
    @Test
    fun testCompareingPasswords(){
        val pass1 = BCrypt.hashpw("Password123!", BCrypt.gensalt())
        println(pass1)
        println(BCrypt.checkpw("123", pass1))

    }

    }
}
