package com.api.ianloops.credit.aplication.dto

import com.api.ianloops.credit.aplication.model.Credit
import com.api.ianloops.credit.aplication.model.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class CreditDto(
    @field:NotNull(message = "Invalid Input")
    val creditValue: BigDecimal,

    @field:NotEmpty(message = "Invalid Input")
    @Future val dayFirstOfInstallment: LocalDate,

    @field:NotEmpty(message = "Invalid Input")
    val numberOfInstallment: Int,

    @field:NotEmpty(message = "Invalid Input")
    val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstallment,
        numberOfInstallment = this.numberOfInstallment,
        customer = Customer(id = this.customerId)
    )
}
