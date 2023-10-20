package com.api.ianloops.credit.aplication.service

import com.api.ianloops.credit.aplication.ennumeration.Status
import com.api.ianloops.credit.aplication.model.Address
import com.api.ianloops.credit.aplication.model.Credit
import com.api.ianloops.credit.aplication.model.Customer
import com.api.ianloops.credit.aplication.repository.CreditRepository
import com.api.ianloops.credit.aplication.service.impl.CreditService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import jakarta.persistence.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK
    lateinit var creditRepository: CreditRepository

    @InjectMockKs
    lateinit var creditService: CreditService

    @Test
    fun `should create a credit`() {
        //given
        val fakeCustomer: Customer = buildCustomer()
        val fakeCredit: Credit = buildCredit(customer = fakeCustomer)
        every { creditRepository.save(any()) } returns fakeCredit
        //when
        val actual: Credit = creditService.save(fakeCredit)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    private fun buildCustomer(
        firstName: String = "João",
        lastName: String = "Silva",
        cpf: String = "26502451071",
        email: String = "mail@mail.com",
        password: String = "1234",
        income: BigDecimal = BigDecimal(1000),
        id: Long = 1L,
        zipCode: String = "12345678",
        street: String = "street 2"
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        income = income,
        id = id,
        address = Address(
            zipCode = zipCode, street = street
        )
    )

    private fun buildCredit(
        creditCode: UUID = UUID.randomUUID(),
        creditValue: BigDecimal = BigDecimal(1000),
        dayFirstInstallment: LocalDate = LocalDate.now().plusDays(20),
        numberOfInstallment: Int = 15,
        status: Status = Status.IN_PROGRESS,
        customer: Customer? = buildCustomer(),
        id: Long? = 1L
    ) = Credit(
        creditCode = creditCode,
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallment = numberOfInstallment,
        status = status,
        customer = customer,
        id = id
    )
}