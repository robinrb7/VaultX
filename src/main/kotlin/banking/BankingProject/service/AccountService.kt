package banking.BankingProject.service

import banking.BankingProject.dto.AccountResponse
import banking.BankingProject.dto.CreateAccountRequest
import banking.BankingProject.dto.FlexiSettingsRequest
import banking.BankingProject.entity.Account
import banking.BankingProject.entity.CurrentAccount
import banking.BankingProject.entity.FixedDepositAccount
import banking.BankingProject.entity.SavingAccount
import banking.BankingProject.repository.AccountRepository
import banking.BankingProject.repository.CustomerRepository
import banking.BankingProject.security.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val customerRepository: CustomerRepository,
    private val jwtUtil: JwtUtil
) {

    fun createAccount(customerId: Long, request: CreateAccountRequest): ResponseEntity<Any>{
        val customer = customerRepository.findById(customerId)
            .orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND,"Customer Not Found") }

        val account = when (request.accountType.uppercase()) {
            "SAVINGS" -> {
                val existingSavings = accountRepository.findByCustomerAndAccountType(customer, "SAVINGS")
                if (existingSavings != null) {
                    return ResponseEntity.badRequest().body(mapOf("error" to "You already have a Savings Account"))
                }
                SavingAccount(
                    interestRate = 0.05,
                    isFlexiEnabled = false,
                    flexiThreshold = 10000.0,
                    minimumBalance = 100.0
                )

            }
            "CURRENT" -> CurrentAccount(
                balance = request.initialDeposit,
                businessName = customer.firstName + " " + customer.lastName,
                customer = customer
            )
            "FIXED_DEPOSIT" -> FixedDepositAccount(
                principalAmount = request.initialDeposit,
                interestRate = 0.12,
                tenureMonths = 12
            )
            else -> throw IllegalArgumentException("Invalid account type")
        }

        val generatedIfsc = "BNK" + (1000..9999).random()
        account.ifscCode = generatedIfsc

        account.balance = request.initialDeposit
        account.customer = customer

        account.accountType = request.accountType.uppercase()

        accountRepository.save(account)
        return ResponseEntity.ok(
            mapOf(
                "message" to "${request.accountType} Account Created Successfully",
                "accountNumber" to account.accountNumber,
                "ifscCode" to account.ifscCode,
                "balance" to account.balance
            )
        )
    }

    fun getAccounts(customerId: Long): List<AccountResponse>{
        val customer = customerRepository.findById(customerId)
            .orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND,"Customer Not Found") }

        val accounts = accountRepository.findByCustomer(customer)

        return accounts.map { account ->
            AccountResponse(
                accountNumber = account.accountNumber,
                balance = account.balance,
                ifscCode = account.ifscCode,
                accountType = account::class.simpleName ?: "Account",
                createdAt = account.customer?.createdAt.toString(),
                ownerName = "${account.customer?.firstName} ${account.customer?.lastName}"
            )
        }
    }


    fun deleteAccount(accountId: Long, request: HttpServletRequest): ResponseEntity<Any> {
        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Missing token"))

        val customerId = jwtUtil.getCustomerIdFromToken(token)
        val customer = customerRepository.findById(customerId).orElseThrow {
            RuntimeException("Customer not found")
        }

        val account = accountRepository.findById(accountId).orElseThrow {
            RuntimeException("Account not found")
        }

        // ✅ Verify ownership (prevent deleting other users’ accounts)
        if (account.customer?.customerId != customer.customerId) {
            return ResponseEntity.status(403).body(mapOf("error" to "Unauthorized to delete this account"))
        }

        accountRepository.delete(account)

        return ResponseEntity.ok(mapOf("message" to "Account deleted successfully"))
    }


    fun updateFlexiSettings(
        customerId: Long,
        request: FlexiSettingsRequest
        ): ResponseEntity<Any>{

        val customer = customerRepository.findById(customerId)
            .orElseThrow{ResponseStatusException(HttpStatus.NOT_FOUND,"Customer Not Found")}

        val savingsAccount = accountRepository.findByCustomer(customer)
            .filterIsInstance<SavingAccount>()
            .firstOrNull()
            ?: return ResponseEntity.badRequest().body(mapOf("error" to "No Savings Account found"))


        // If Flexi is being disabled
        if(!request.enableFlexi){
            savingsAccount.isFlexiEnabled = false
            savingsAccount.flexiThreshold = 0.00

            accountRepository.save(savingsAccount)

            return ResponseEntity.ok(mapOf(
                "message" to "Flexi Savings disabled",
                "isFlexiEnabled" to false,
                "currentThreshold" to 0.00
            ))

        }

        if (savingsAccount.isFlexiEnabled == request.enableFlexi) {
            return ResponseEntity.ok(mapOf("message" to "No changes made. Flexi status is already ${request.enableFlexi}"))
        }

        val threshold = request.flexiThreshold
            ?: return ResponseEntity.badRequest().body(mapOf("error" to "Flexi threshold is required when enabling Flexi"))


        if(threshold < MIN_FLEXI_THRESHOLD){
            return ResponseEntity.badRequest().body(mapOf("error" to "Flexi threshold must be at least ₹10,000"))
        }

        if (threshold > MAX_FLEXI_THRESHOLD) {
            return ResponseEntity.badRequest()
                .body(mapOf("error" to "Flexi threshold cannot exceed ₹1,00,000"))
        }

        // 🧩 Update fields
        savingsAccount.isFlexiEnabled = true
        savingsAccount.flexiThreshold = threshold

        accountRepository.save(savingsAccount)


        return ResponseEntity.ok(mapOf(
            "message" to "Flexi Savings enabled with threshold ₹${request.flexiThreshold}",
            "isFlexiEnabled" to true,
            "currentThreshold" to threshold
        ))

    }

    companion object {
        const val MIN_FLEXI_THRESHOLD = 10000.00
        const val MAX_FLEXI_THRESHOLD = 100000.00
    }

}

