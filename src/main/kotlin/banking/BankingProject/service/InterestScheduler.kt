package banking.BankingProject.service

import banking.BankingProject.entity.FixedDepositAccount
import banking.BankingProject.entity.FixedDepositStatus
import banking.BankingProject.entity.SavingAccount
import banking.BankingProject.repository.AccountRepository
import banking.BankingProject.repository.FixedDepositRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.math.pow

@Service
class InterestScheduler(
    private val accountRepository: AccountRepository,
    private val fixedDepositRepository: FixedDepositRepository
) {

    /**
     * 💰 Apply daily interest for all active savings accounts.
     * This runs every night at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    fun applyDailySavingsInterest() {
        val savingsAccounts = accountRepository.findAll()
            .filter { it.accountType == "SAVINGS" }

        savingsAccounts.forEach { account ->
            // Daily simple interest: (Annual Interest / 365)
            val dailyInterest = account.balance * (0.05 / 365)
            account.balance += dailyInterest
        }

        accountRepository.saveAll(savingsAccounts)
        println("✅ Daily interest applied to ${savingsAccounts.size} savings accounts.")
    }



    @Scheduled(cron = "0 0 2 * * ?") // runs daily at 2 AM
    fun handleFlexiSavings() {
        val savingsAccounts = accountRepository.findAll()
            .filter { it.accountType == "SAVINGS" && (it as? SavingAccount)?.isFlexiEnabled == true }

        savingsAccounts.forEach { acc ->
            val savingAcc = acc as SavingAccount

            if (savingAcc.balance > savingAcc.flexiThreshold) {
                val excessAmount = savingAcc.balance - savingAcc.flexiThreshold

                // 🧮 Reduce the savings account balance
                savingAcc.balance -= excessAmount

                // 💰 Create a new mini Fixed Deposit for the excess
                val miniFd = FixedDepositAccount(
                    principalAmount = excessAmount,
                    interestRate = 0.07, // slightly higher rate for Flexi FDs
                    tenureMonths = 3,
                    autoRenewal = false
                )

                // Link to the same customer
                miniFd.customer = savingAcc.customer
                miniFd.accountType = "FIXED_DEPOSIT"
                miniFd.balance = 0.0 // FD balance always 0, principalAmount is stored separately

                fixedDepositRepository.save(miniFd)
                accountRepository.save(savingAcc)

                println("💡 Flexi FD created for ${savingAcc.customer?.username}: ₹$excessAmount moved from savings to FD")
            }
        }
    }




    /**
     * 🔁 Check FDs for maturity and handle auto-renewal or closure.
     * Runs every day at 1 AM.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    fun processFixedDeposits() {
        val today = LocalDate.now()
        val fixedDeposits = fixedDepositRepository.findAll()

        fixedDeposits.forEach { fd ->
            if (fd.maturityDate.isBefore(today) || fd.maturityDate.isEqual(today)) {
                if (fd.autoRenewal) {
                    handleAutoRenewal(fd)
                } else {
                    handleMaturityPayout(fd)
                }
            }
        }
    }

    /**
     * ♻️ Handles FD auto renewal.
     */
    private fun handleAutoRenewal(fd: FixedDepositAccount) {
        val interestEarned = calculateCompoundInterest(fd.principalAmount, fd.interestRate, fd.tenureMonths)
        val newPrincipal = fd.principalAmount + interestEarned

        fd.principalAmount = newPrincipal
        fd.startDate = LocalDate.now()
        fd.maturityDate = LocalDate.now().plusMonths(fd.tenureMonths.toLong())
        fd.fdStatus = FixedDepositStatus.ACTIVE

        fixedDepositRepository.save(fd)
        println("🔁 FD auto-renewed for account ${fd.accountNumber} with new principal ₹$newPrincipal")
    }


    private fun handleMaturityPayout(fd: FixedDepositAccount) {
        val interestEarned = calculateCompoundInterest(fd.principalAmount, fd.interestRate, fd.tenureMonths)
        val maturityAmount = fd.principalAmount + interestEarned

        val linkedSavings = fd.customer?.accounts?.firstOrNull { it.accountType == "SAVINGS" }

        if (linkedSavings != null) {
            linkedSavings.balance += maturityAmount
            accountRepository.save(linkedSavings)
            println("💰 Credited ₹$maturityAmount to linked savings account ${linkedSavings.accountNumber}")
        } else {
            println("⚠️ No linked savings account found for FD ${fd.accountNumber}")
        }

        // Instead of deleting, mark FD as matured
        fd.fdStatus = FixedDepositStatus.MATURED
        fixedDepositRepository.save(fd)

        println("✅ FD ${fd.accountNumber} marked as MATURED.")
    }



    /**
     * 📈 Compound Interest Formula
     * A = P * (1 + r/4)^(4 * t)  [Quarterly compounding]
     */
    private fun calculateCompoundInterest(principal: Double, annualRate: Double, tenureMonths: Int): Double {
        val years = tenureMonths / 12.0
        val n = 4 // quarterly compounding
        return principal * (1 + (annualRate / n)).pow(n * years) - principal
    }


}