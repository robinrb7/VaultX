package banking.BankingProject.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table


@Entity
@Table(name = "saving_account")
@PrimaryKeyJoinColumn(name="account_no")
open class SavingAccount(

    @Column(nullable = false)
    var interestRate: Double = 0.05,

    @Column(nullable = false)
    var isFlexiEnabled: Boolean = false,

    @Column(nullable = false)
    var flexiThreshold : Double = 10000.00,

    @Column(nullable = false)
    val minimumBalance : Double = 100.00


): Account(){

    //only used by Hibernate internally to load data from database to entity
    constructor() : this(
        0.05,      // interestRate
        false,     // isFlexiEnabled
        10000.00,   // flexiThreshold
        100.00     // minimumBalance
    )

    //custom constructor for service class
    constructor(
        balance: Double,
        customer: Customer
    ) : this() {
        this.balance = balance
        this.customer = customer
    }
}