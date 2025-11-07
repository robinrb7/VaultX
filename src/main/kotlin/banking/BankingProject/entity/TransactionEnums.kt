package banking.BankingProject.entity

enum class TransactionType{
    Deposit,
    Withdrawal,
    Transfer
}

enum class TransactionStatus{
    Success,
    Pending,
    Failed
}