package banking.BankingProject.dto

data class CreateAccountRequest(
    val accountType: String,            // "SAVINGS", "CURRENT", "FIXED_DEPOSIT"s
    val initialDeposit: Double = 0.0
)
