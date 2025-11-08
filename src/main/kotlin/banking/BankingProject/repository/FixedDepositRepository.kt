package banking.BankingProject.repository

import banking.BankingProject.entity.FixedDepositAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FixedDepositRepository: JpaRepository<FixedDepositAccount,Long> {
}