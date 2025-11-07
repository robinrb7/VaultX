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
        20000.00,
        5000.00,
        ""
    )
}