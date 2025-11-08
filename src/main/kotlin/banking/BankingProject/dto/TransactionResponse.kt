package banking.BankingProject.dto

data class TransactionResponse(
    val transactionId: Long,
    val fromAccount: Long,
    val toAccount: Long,
    val amount: Double,
    val status: String,
    val timestamp: String
)
