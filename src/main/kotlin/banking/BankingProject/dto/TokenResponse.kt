package banking.BankingProject.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer"
)
