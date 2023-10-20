package com.api.ianloops.credit.aplication.dto

import com.api.ianloops.credit.aplication.model.Customer
import java.math.BigDecimal

data class CustomerView(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val email: String,
    val zipcode: String,
    val street: String
) {
    constructor(customer: Customer): this(
        id = customer.id,
        firstName = customer.firstName,
        lastName = customer.lastName,
        cpf = customer.cpf,
        income = customer.income,
        email = customer.email,
        zipcode = customer.address.zipCode,
        street = customer.address.street
    )
}
