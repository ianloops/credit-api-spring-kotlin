package com.api.ianloops.credit.aplication.service

import com.api.ianloops.credit.aplication.model.Customer

interface ICustomerService {
    fun save(customer: Customer):Customer

    fun findById(id:Long):Customer

    fun delete(id:Long)
}