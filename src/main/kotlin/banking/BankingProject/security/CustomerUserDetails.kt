package banking.BankingProject.security

import banking.BankingProject.entity.Customer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomerUserDetails(private val customer: Customer): UserDetails {
    override fun getAuthorities(): Collection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return customer.hashedPassword
    }

    override fun getUsername(): String {
        return customer.username
    }

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true

    fun getCustomerId(): Long{
        return customer.customerId
    }


}