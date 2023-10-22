package com.api.ianloops.credit.application.dto.request

import com.api.ianloops.credit.application.model.Credit
import com.api.ianloops.credit.application.model.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:Positive(message = "Invalid Input")
    val creditValue: BigDecimal,

    @Future val dayFirstInstallment: LocalDate,

    @field:Min(value = 1)
    @field:Max(value = 48)
    val numberOfInstallment: Int,

    @field:NotNull(message = "Invalid Input")
    val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstallment = this.numberOfInstallment,
        customer = Customer(id = this.customerId)
    )
}
