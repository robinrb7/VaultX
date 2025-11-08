package banking.BankingProject

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BankingProjectApplication

fun main(args: Array<String>) {
	runApplication<BankingProjectApplication>(*args)
}
