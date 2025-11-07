package banking.BankingProject.entity

enum class PaymentStatus{
    SUCCESS,
    PENDING,
    FAILED
}

enum class PaymentMethod {
    AUTO_DEBIT,     // EMI auto-deducted from account
    MANUAL_TRANSFER,// Paid manually
    CASH,           // Paid at branch (rare)
    ONLINE          // UPI / Netbanking
}