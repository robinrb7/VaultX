package banking.BankingProject.service

import banking.BankingProject.repository.CustomerRepository
import banking.BankingProject.security.CustomerUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomerUserDetailService(
    private val customerRepository: CustomerRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val customer = customerRepository.findByUsername(username)
            ?:throw UsernameNotFoundException("No user was found with username: ${username}")
        return CustomerUserDetails(customer)
    }

    fun loadUserById(customerId: Long): UserDetails {
        val customer = customerRepository.findById(customerId)
            .orElseThrow { UsernameNotFoundException("User not found with id: $customerId") }
        return CustomerUserDetails(customer)
    }

}