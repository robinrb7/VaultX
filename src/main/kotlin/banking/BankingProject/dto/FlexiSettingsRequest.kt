package banking.BankingProject.dto

data class FlexiSettingsRequest(
    val enableFlexi: Boolean,
    val flexiThreshold: Double? = null
)
