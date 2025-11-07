package banking.BankingProject.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name="customers")
open class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val customerId: Long = 0,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val hashedPassword: String,

    @Column(nullable = false, unique = true)
    val email: String,


    @Column(nullable = false)
    val firstName: String,

    val middleName: String? = null,

    @Column(nullable = false)
    val lastName: String,

    @ElementCollection
    @CollectionTable(
        name = "customer_phone_numbers",
        joinColumns = [JoinColumn(name = "customerId")]
    )
    @Column(name="phone_number", nullable = false)
    val phoneNumbers: Set<String> = setOf(),

    @Column(nullable = false)
    val dob: LocalDate,

    val age: Int? = null,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "customer",cascade = [CascadeType.ALL], orphanRemoval = true)
    var accounts: MutableList<Account> = mutableListOf(),

    @OneToMany(mappedBy = "customer", cascade = [CascadeType.ALL], orphanRemoval = true)
    var beneficiaries: MutableList<Beneficiary> = mutableListOf(),

    @OneToMany(mappedBy = "customer",cascade = [CascadeType.ALL], orphanRemoval = true)
    var otpVerifications: MutableList<OtpVerification> = mutableListOf(),

){
    constructor() : this(
        0, "", "", "", "", null, "", setOf(), LocalDate.now(), null, LocalDateTime.now(), mutableListOf(), mutableListOf(), mutableListOf()
    )

}