package com.api.ianloops.credit.aplication.repository

import com.api.ianloops.credit.aplication.model.Address
import com.api.ianloops.credit.aplication.model.Credit
import com.api.ianloops.credit.aplication.model.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CreditRepositoryTest {
    @Autowired lateinit var creditRepository: CreditRepository
    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var credit1: Credit
    private lateinit var credit2: Credit

    @BeforeEach fun setup(){
        customer = testEntityManager.persist(buildCustomer())
        credit1 = testEntityManager.persist(buildCredit(customer = customer))
        credit2 = testEntityManager.persist(buildCredit(customer = customer))
    }

    @Test
    fun `should find credit by creditCode`(){
        //given
        val creditCode1 = UUID.fromString("a8a09d4d-78a6-4d5f-a542-316e9a9743d2")
        val creditCode2 = UUID.fromString("5231321e-2ba0-4394-bdf9-52e3566b8a1b")
        credit1.creditCode = creditCode1
        credit2.creditCode = creditCode2
        //when
        val fakeCredit1 = creditRepository.findByCreditCode(creditCode1)!!
        val fakeCredit2 = creditRepository.findByCreditCode(creditCode2)!!
        //then
        Assertions.assertThat(fakeCredit1).isNotNull
        Assertions.assertThat(fakeCredit2).isNotNull
        Assertions.assertThat(fakeCredit1).isSameAs(credit1)
        Assertions.assertThat(fakeCredit2).isSameAs(credit2)
    }

    @Test
    fun `should find all credits by customer id`(){
        //given
        val customerId: Long = 1L
        //when
        val creditList : List<Credit> = creditRepository.findAllByCustomerId(customerId)
        //then
        Assertions.assertThat(creditList).isNotEmpty
        Assertions.assertThat(creditList).size().isEqualTo(2)
        Assertions.assertThat(creditList).contains(credit1,credit2)
    }

    private fun buildCustomer(
        firstName: String = "João",
        lastName: String = "Silva",
        cpf: String = "26502451071",
        email: String = "mail@mail.com",
        password: String = "1234",
        income: BigDecimal = BigDecimal.valueOf(1000),
        zipCode: String = "12345678",
        street: String = "street 2"
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        income = income,
        address = Address(
            zipCode = zipCode, street = street
        )
    )

    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(1000),
        dayFirstInstallment: LocalDate = LocalDate.now().plusDays(30),
        numberOfInstallment: Int = 15,
        customer: Customer,
    ) = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallment = numberOfInstallment,
        customer = customer,
    )
}