package banking.BankingProject.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name= "fixed_deposit_account")
@PrimaryKeyJoinColumn(name="account_no")
open class FixedDepositAccount(

    @Column(nullable = false)
    val principalAmount: Double,

    @Column(nullable = false)
    val interestRate: Double = 0.12,

    @Column(nullable = false)
    val tenureMonths: Int = 12,

    @Column(nullable = false)
    val startDate: LocalDate = LocalDate.now(),

    @Column(nullable = false)
    val maturityDate: LocalDate = startDate.plusMonths(tenureMonths.toLong()),

    @Column(nullable = false)
    val autoRenewal: Boolean = false

): Account(){

    constructor() : this(
        0.0,          // principalAmount
        0.12,         // interestRate
        12,           // tenureMonths
        LocalDate.now(), // startDate
        LocalDate.now().plusMonths(12), // maturityDate
        false         // autoRenewal
    )
}