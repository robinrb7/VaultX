package banking.BankingProject.controller

import banking.BankingProject.dto.AccountResponse
import banking.BankingProject.dto.CreateAccountRequest
import banking.BankingProject.dto.FlexiSettingsRequest
import banking.BankingProject.security.JwtUtil
import banking.BankingProject.service.AccountService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/account")
class AccountController(
    private val accountService: AccountService,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/create")
    fun createAccount(
        @RequestBody requestBody: CreateAccountRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<Any>{

        val customerId = extractCustomerIdFromToken(httpRequest)
        return accountService.createAccount(customerId,requestBody)
    }

    @GetMapping("accounts")
    fun getAllAccounts(
        httpRequest: HttpServletRequest
    ): ResponseEntity<List<AccountResponse>>{
        val customerId = extractCustomerIdFromToken(httpRequest)
        return ResponseEntity.ok(accountService.getAccounts(customerId))
    }


    @DeleteMapping("/delete/{accountId}")
    fun deleteAccount(
        @PathVariable accountId: Long,
        httpRequest: HttpServletRequest
    ): ResponseEntity<Any>{
        accountService.deleteAccount(accountId,httpRequest)

        return ResponseEntity.ok("Account deleted successfully")

    }

    @PatchMapping("/flexi/update")
    fun updateFlexi(
        @RequestBody body: FlexiSettingsRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<Any>{

        val customerId = extractCustomerIdFromToken(httpRequest)
        return accountService.updateFlexiSettings(customerId,body)

    }



    private fun extractCustomerIdFromToken(request: HttpServletRequest): Long{
        val header = request.getHeader("Authorization")
            ?: throw IllegalArgumentException("Authorization header missing")

        if(!header.startsWith("Bearer ")){
            throw IllegalArgumentException("Invalid Authorization header format")
        }

        val token = header.removePrefix("Bearer ")

        if(!jwtUtil.validateToken(token)){
            throw IllegalArgumentException("Invalid or Expired token")
        }

        return jwtUtil.getCustomerIdFromToken(token)

    }

}