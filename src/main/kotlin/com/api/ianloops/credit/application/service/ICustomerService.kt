package com.api.ianloops.credit.application.service

import com.api.ianloops.credit.application.model.Customer

interface ICustomerService {
    fun save(customer: Customer):Customer

    fun findById(id:Long):Customer

    fun delete(id:Long)
}