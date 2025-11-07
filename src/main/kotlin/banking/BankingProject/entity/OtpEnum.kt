package banking.BankingProject.entity

import jakarta.persistence.Enumerated

enum class OtpPurpose{
    LOGIN,
    FUND_TRANSFER,
    BENEFICIARY_ADD,
    PASSWORD_RESET,
    PIN_CHANGE

}

enum class OtpStatus{
    PENDING,
    VERIFIED,
    EXPIRED
}