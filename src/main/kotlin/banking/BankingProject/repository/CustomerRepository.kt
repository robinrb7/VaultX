package banking.BankingProject.repository

import banking.BankingProject.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: JpaRepository<Customer,Long>{
    fun findByEmail(email: String): Customer?
    fun findByUsername(username: String): Customer?
    fun existsByEmail(email: String): Boolean
    fun existsByUsername(username: String): Boolean

}