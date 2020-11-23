package com.randochat.main
//
//import com.randochat.main.database.AccountRepository
//import com.randochat.main.endpoints.RegisterAccount
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
//import org.springframework.boot.test.context.SpringBootTest
//
//
//@SpringBootTest
//@AutoConfigureTestDatabase
//class TestDataBase @Autowired constructor(val accountRepository: AccountRepository, @Autowired val registerController: RegisterAccount){
//
//    @Test
//    fun test1(){
//        println(accountRepository.findByUserName("userName"))
////        assert(registerController)
//    }
//    @Test
//    fun test1(@Autowired mockserver: MockMvc){
//        mockserver.perform(MockMvcRequestBuilders.get("/accounts/create", )
//                .header("newAccount", AccountFormatter.encodeAccount("email@email.com\\username\\VALIDPASSWORD856!\\"))
//                .header("code", AuthCodes.codeAccess[0]))
//
//    }
//
//}