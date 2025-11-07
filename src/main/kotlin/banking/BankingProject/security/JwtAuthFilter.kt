package banking.BankingProject.security

import banking.BankingProject.service.CustomerUserDetailService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailService: CustomerUserDetailService

): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")

        if(header != null && header.startsWith("Bearer ")){
            val token = header.removePrefix("Bearer ")

            try {
                if(jwtUtil.validateToken(token)){
                    val username = jwtUtil.getUsernameFromToken(token)
                    val userDetails = userDetailService.loadUserByUsername(username)

                    val authentication = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )

                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }

            }catch (e: Exception){
                println("Authentication Failed: ${e.message}")
            }
        }

        filterChain.doFilter(request,response)

    }
}