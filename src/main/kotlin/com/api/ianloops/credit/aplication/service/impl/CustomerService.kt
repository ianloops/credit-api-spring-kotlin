package com.api.ianloops.credit.aplication.service.impl

import com.api.ianloops.credit.aplication.exception.BusinessException
import com.api.ianloops.credit.aplication.model.Customer
import com.api.ianloops.credit.aplication.repository.CustomerRepository
import com.api.ianloops.credit.aplication.service.ICustomerService
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
) : ICustomerService {

    override fun save(customer: Customer): Customer = this.customerRepository.save(customer)

    override fun findById(id: Long): Customer = this.customerRepository.findById(id).orElseThrow {
        throw BusinessException("Id $id not found")
    }

    override fun delete(id: Long) {
        val customer : Customer = this.findById(id)
        this.customerRepository.delete(customer)
    }

}