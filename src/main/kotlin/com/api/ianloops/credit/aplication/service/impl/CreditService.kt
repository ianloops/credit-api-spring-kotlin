package com.api.ianloops.credit.aplication.service.impl

import com.api.ianloops.credit.aplication.exception.BusinessException
import com.api.ianloops.credit.aplication.model.Credit
import com.api.ianloops.credit.aplication.repository.CreditRepository
import com.api.ianloops.credit.aplication.service.ICreditService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
) : ICreditService {

    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> {
        return this.creditRepository.findAllByCustomer(customerId)
    }

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit: Credit = this.creditRepository.findByCreditCode(creditCode)
            ?: throw RuntimeException("Credit Code $creditCode does not exist")
        return if (credit.customer?.id == customerId) credit else throw BusinessException ("Contact Admin")
    }
}