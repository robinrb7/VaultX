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
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "loan_payment")
open class LoanPayment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val paymentId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    val loan: Loan? = null,

    @Column(nullable = false)
    val paymentAmount: Double,

    @Column(nullable = false)
    val paymentDate: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val paymentMethod: PaymentMethod = PaymentMethod.AUTO_DEBIT,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,

    @Column(nullable = true)
    val remarks: String? = null

){

    constructor() : this(
        0,
        null,
        0.0,
        LocalDateTime.now(),
        PaymentMethod.AUTO_DEBIT,
        PaymentStatus.PENDING,
        null
    )
}