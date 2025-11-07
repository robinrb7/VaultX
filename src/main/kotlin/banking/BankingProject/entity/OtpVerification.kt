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
@Table(name = "otp_verifications")
open class OtpVerification(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val otpId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    val customer: Customer? = null,

    @Column(nullable = false)
    val otpCode: String,

    // Why the OTP was generated (login, fund transfer, etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val purpose: OtpPurpose,

    // Whether it's still valid, verified, or expired
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OtpStatus = OtpStatus.PENDING,



    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val expiresAt: LocalDateTime = createdAt.plusMinutes(5)

){

    constructor() : this(
        0,
        null,
        "",
        OtpPurpose.LOGIN,
        OtpStatus.PENDING,
        LocalDateTime.now(),
        LocalDateTime.now().plusMinutes(5)
    )
}