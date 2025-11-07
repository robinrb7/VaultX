package banking.BankingProject.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "account_security")
open class AccountSecurity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val securityId: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", nullable = false)
    val account: Account? = null,

    @Column(nullable = false)
    val hashedTransitPin: String,

    @Column(nullable = false)
    val failedAttempts: Int = 0,

    @Column(nullable = false)
    val accountLocked: Boolean = false,

    @Column(nullable = true)
    val lockUntil: LocalDateTime? = null,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = true)
    val lastUpdatedAt: LocalDateTime? = null,

){
    constructor() : this(
        0,             // securityId
        null,          // account (will be injected later)
        "",            // hashedTransitPin
        0,             // failedAttempts
        false,         // accountLocked
        null,          // lockUntil
        LocalDateTime.now(), // createdAt
        null           // lastUpdatedAt
    )
}