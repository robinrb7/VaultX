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
@Table(name = "beneficiaries")
open class Beneficiary(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val beneficiaryId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    val customer: Customer? = null,

    @Column(nullable = false)
    val beneficiaryName: String,

    @Column(nullable = false)
    val accountNumber: Long,

    @Column(nullable = false)
    val ifscCode: String,

    @Column(nullable = false)
    val nickname: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val beneficiaryVerified: BeneficiaryVerified = BeneficiaryVerified.PENDING,

    @Column(nullable = false)
    val addedAt: LocalDateTime = LocalDateTime.now()

){
    constructor() : this(
        0,
        null,
        "",
        0L,
        "",
        null,
        BeneficiaryVerified.PENDING,
        LocalDateTime.now()
    )
}