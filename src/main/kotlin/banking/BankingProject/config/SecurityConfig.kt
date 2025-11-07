package banking.BankingProject.config

import banking.BankingProject.security.JwtAuthFilter
import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
) {

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain{
        return httpSecurity
            .csrf{it.disable()}
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests{auth ->
                auth
                    .requestMatchers(
                        "/auth/**").permitAll() // registration and login are public
                    .dispatcherTypeMatchers(
                        DispatcherType.ERROR,
                        DispatcherType.FORWARD
                    ).permitAll()
                    .anyRequest().authenticated() // everything else needs to be authenticated first
            }
            .exceptionHandling {
                it.authenticationEntryPoint ( HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED) )
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}