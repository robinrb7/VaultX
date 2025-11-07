package banking.BankingProject.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table

@Entity
@Table(name= "current_account")
@PrimaryKeyJoinColumn(name="account_no")
open class CurrentAccount(

    @Column(nullable = false)
    val overdraftLimit: Double = 20000.00,

    @Column(nullable = false)
    val minimumBalance: Double = 5000.00,

    @Column(nullable = false)
    val businessName: String

): Account(){

    constructor() : this(
        500000.00,
        10000.00,
        ""
    )

    // ✅ Used in AccountService
    constructor(balance: Double, businessName: String, customer: Customer)
            : this(500000.00, 10000.00, businessName) {
        this.balance = balance
        this.customer = customer
    }
}