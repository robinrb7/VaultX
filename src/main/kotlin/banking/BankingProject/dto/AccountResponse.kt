package banking.BankingProject.dto

data class AccountResponse(
    val accountNumber: Long,
    val balance: Double,
    val ifscCode: String,
    val accountType: String,
    val createdAt: String,
    val ownerName: String
)
