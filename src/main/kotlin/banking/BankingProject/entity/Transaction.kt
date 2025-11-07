package banking.BankingProject.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
open class Transaction(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val transactionId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_number", nullable = false)
    val sourceAccount : Account? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_number", nullable = false)
    val destinationAccount : Account? = null,

    @Column(nullable= false)
    val amount: Double,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val transaction_type: TransactionType,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val transaction_status: TransactionStatus = TransactionStatus.Success,

    @Column(nullable = false)
    val timestamp: LocalDateTime = LocalDateTime.now(),

    val remarks: String? = null

){

    constructor() : this(
        0,
        null,
        null,
        0.0,
        TransactionType.Transfer,
        TransactionStatus.Success,
        LocalDateTime.now(),
        null
    )
}