package banking.BankingProject.repository

import banking.BankingProject.entity.Account
import banking.BankingProject.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<Account,Long> {

    // Fetch all accounts owned by a particular customer
    fun findByCustomer(customer: Customer):List<Account>

    //to find if a person holds any account or not
    fun existsByCustomer(customer: Customer): Boolean

    fun findByCustomerAndAccountType(customer: Customer, accountType: String): Account?

}