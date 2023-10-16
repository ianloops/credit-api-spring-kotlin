package com.api.ianloops.credit.aplication.dto

import com.api.ianloops.credit.aplication.ennumeration.Status
import com.api.ianloops.credit.aplication.model.Credit
import jakarta.validation.constraints.Email
import java.math.BigDecimal
import java.util.*

data class CreditView (
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberOfInstallment: Int,
    val status: Status,
    val emailCustomer: String?,
    val incomeCustomer: BigDecimal?
){
    constructor(credit: Credit): this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallment = credit.numberOfInstallment,
        status = credit.status,
        emailCustomer = credit.customer?.email,
        incomeCustomer = credit.customer?.income
    )
}
