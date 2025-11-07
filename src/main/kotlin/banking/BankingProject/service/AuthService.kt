package banking.BankingProject.service

import banking.BankingProject.dto.LoginRequest
import banking.BankingProject.dto.RegisterRequest
import banking.BankingProject.dto.TokenResponse
import banking.BankingProject.entity.Customer
import banking.BankingProject.repository.CustomerRepository
import banking.BankingProject.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val customerRepository: CustomerRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    fun register(request: RegisterRequest): ResponseEntity<Any>{
        if(customerRepository.existsByUsername(request.username)){
            return ResponseEntity.badRequest().body(mapOf("error" to "Username already exists"))
        }

        if(customerRepository.existsByEmail(request.email)){
            return ResponseEntity.badRequest().body(mapOf("error" to "Email already registered to an account"))
        }

        val hashPassword = passwordEncoder.encode(request.password)!!


        val phoneNumbers = mutableSetOf<String>()
        phoneNumbers.add(request.phoneNumber1)
        request.phoneNumber2?.let { phoneNumbers.add(it) }

        val customer = Customer(
            username = request.username,
            hashedPassword = hashPassword,
            email = request.email,
            firstName = request.firstName,
            middleName = request.middleName,
            lastName = request.lastName,
            phoneNumbers = phoneNumbers,
            dob = request.dob

        )

        customerRepository.save(customer)
        return ResponseEntity.ok(mapOf("message" to "Registration Successful"))

    }

    fun login(request: LoginRequest): ResponseEntity<Any>{

        val customer = customerRepository.findByUsername(request.username)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Invalid username Or No Customer registered"))

        if(!passwordEncoder.matches(request.password,customer.hashedPassword)){
            return ResponseEntity.status(401).body(mapOf("error" to "Invalid username or password"))
        }

        val accessToken = jwtUtil.generateAccessToken(request.username,customer.customerId)
        val refreshToken = jwtUtil.generateRefreshToken(request.username,customer.customerId)

        return ResponseEntity.ok(TokenResponse(accessToken,refreshToken))
    }

    fun refresh(refreshToken: String): ResponseEntity<Any>{
        if(!jwtUtil.validateToken(refreshToken)){
            return ResponseEntity.status(401).body(mapOf("error" to "Invalid or Expired Refresh Token"))
        }

        val username = jwtUtil.getUsernameFromToken(refreshToken)
        val customerId = jwtUtil.getCustomerIdFromToken(refreshToken)

        val newAccessToken = jwtUtil.generateAccessToken(username,customerId)
        val newRefreshToken = jwtUtil.generateRefreshToken(username,customerId)

        return ResponseEntity.ok(TokenResponse(newAccessToken,newRefreshToken))
    }
}