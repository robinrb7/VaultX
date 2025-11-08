package banking.BankingProject.dto

data class TransactionRequest(
    val fromAccount: Long,
    val toAccount: Long,
    val amount: Double,
    val remarks: String? = null
)
