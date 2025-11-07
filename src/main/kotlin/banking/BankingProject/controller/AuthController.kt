package banking.BankingProject.controller

import banking.BankingProject.dto.LoginRequest
import banking.BankingProject.dto.RegisterRequest
import banking.BankingProject.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun registerUser(
        @RequestBody body: RegisterRequest
    ): ResponseEntity<Any>{
        return authService.register(body)
    }

    @PostMapping("/login")
    fun loginUser(
        @RequestBody body: LoginRequest
    ): ResponseEntity<Any>{
        return authService.login(body)
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @RequestBody body: Map<String,String>
    ): ResponseEntity<Any>{
        val refreshToken = body["refreshToken"] ?: return ResponseEntity.badRequest().body(mapOf("error" to "Refresh Token required"))
        return authService.refresh(refreshToken)
    }


}