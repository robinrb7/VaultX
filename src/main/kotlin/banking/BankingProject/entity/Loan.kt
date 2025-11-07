package banking.BankingProject.entity

import jakarta.persistence.CascadeType
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
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate
import kotlin.math.pow

@Entity
@Table(name = "loans")
open class Loan(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val loanId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    val customer: Customer? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_account_no", nullable = false)
    val linkedAccount: Account? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val loanType: LoanType,

    @Column(nullable = false)
    val principalAmount: Double,

    @Column(nullable = false)
    val interestRate: Double, // Annual

    @Column(nullable = false)
    val tenureMonths: Int,

    @Column(nullable = false)
    val startDate: LocalDate = LocalDate.now(),

    @Column(nullable = false)
    val endDate: LocalDate = startDate.plusMonths(tenureMonths.toLong()),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val loanStatus: LoanStatus = LoanStatus.ACTIVE,

    @OneToMany(mappedBy = "loan", cascade = [CascadeType.ALL], orphanRemoval = true)
    val loanPayments: MutableList<LoanPayment> = mutableListOf()

){

    constructor() : this(
        0,
        null,
        null,
        LoanType.PERSONAL,
        0.0,
        0.0,
        12,
        LocalDate.now(),
        LocalDate.now().plusMonths(12),
        LoanStatus.ACTIVE,
        mutableListOf()
    )
}