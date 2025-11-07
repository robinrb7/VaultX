package banking.BankingProject.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import kotlin.random.Random

@Entity
@Table(name="accounts")
@Inheritance(strategy = InheritanceType.JOINED)
open class Account(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val accountNumber: Long = 0,

    @Column(nullable = false, unique = true)
    val ifscCode: String = getIfscCode(),

    @Column(nullable= false)
    val balance: Double = 0.0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    val customer : Customer? = null,

    @OneToOne(mappedBy = "account", cascade = [CascadeType.ALL], orphanRemoval = true)
    val accountSecurity: AccountSecurity? = null,

){
    companion object{
        fun getIfscCode(): String{
            val branchCode = Random.nextInt(1000,9999)
            return "BNK0$branchCode"

        }
    }

    constructor() : this(
        0,                    // accountNumber
        "BNK0000",            // ifscCode (dummy value)
        0.0,                  // balance
        null,                 // customer (will be injected later)
        null                  // accountSecurity
    )

}