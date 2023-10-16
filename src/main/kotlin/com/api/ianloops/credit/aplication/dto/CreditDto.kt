package com.api.ianloops.credit.aplication.dto

import com.api.ianloops.credit.aplication.model.Credit
import com.api.ianloops.credit.aplication.model.Customer
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class CreditDto(
    val creditValue: BigDecimal,
    val dayFirstOfInstallment: LocalDate,
    val numberOfInstallment: Int,
    val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstallment,
        numberOfInstallment = this.numberOfInstallment,
        customer = Customer(id = this.customerId)
    )
}
