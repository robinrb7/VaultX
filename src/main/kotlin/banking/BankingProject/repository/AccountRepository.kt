package banking.BankingProject.repository

import banking.BankingProject.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<Account,Long> {

}