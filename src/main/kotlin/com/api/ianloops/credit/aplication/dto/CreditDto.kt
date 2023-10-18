package com.api.ianloops.credit.aplication.dto

import com.api.ianloops.credit.aplication.model.Credit
import com.api.ianloops.credit.aplication.model.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "Invalid Input")
    val creditValue: BigDecimal,

    @Future val dayFirstOfInstallment: LocalDate,

    @field:NotNull(message = "Invalid Input")
    val numberOfInstallment: Int,

    @field:NotNull(message = "Invalid Input")
    val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstallment,
        numberOfInstallment = this.numberOfInstallment,
        customer = Customer(id = this.customerId)
    )
}
