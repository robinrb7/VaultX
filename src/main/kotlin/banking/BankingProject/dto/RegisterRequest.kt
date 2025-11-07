package banking.BankingProject.dto

import java.time.LocalDate

data class RegisterRequest(
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
    val phoneNumber1: String,
    val phoneNumber2: String?,
    val dob: LocalDate
)
