package com.api.ianloops.credit.application.controller

import com.api.ianloops.credit.application.dto.request.CreditDto
import com.api.ianloops.credit.application.dto.request.CustomerDto
import com.api.ianloops.credit.application.model.Credit
import com.api.ianloops.credit.application.repository.CreditRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL_CUSTOMER: String = "/customers"
        const val URL: String = "/credits"
    }

    @BeforeEach
    fun setup() {
        creditRepository.deleteAll()
        val customerDto: CustomerDto = CustomerResourceTest.buildCustomerDto()
        val customerDtoAsString: String = objectMapper.writeValueAsString(customerDto)
        mockMvc.perform(
            MockMvcRequestBuilders.post(CreditResourceTest.URL_CUSTOMER).contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoAsString)
        )
    }

    @AfterEach
    fun tearDown() = creditRepository.deleteAll()

    @Test
    fun `should save credit and return 201 status`() {
        //given
        val creditDto: CreditDto = buildCreditDto()
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(CreditResourceTest.URL).contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(5000))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallment").value(15))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("mail@mail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(1000))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save more than 48 installments and return 400 status`() {
        //given
        val creditDto: CreditDto = buildCreditDto(numberOfInstallment = 49)
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(CreditResourceTest.URL).contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request, see documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save credit with value 0 and return 409 status`() {
        //given
        val creditDto: CreditDto = buildCreditDto(creditValue = BigDecimal.ZERO);
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(CreditResourceTest.URL).contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request, see documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find all customer credits and return 200 status`() {
        //given
        val credit1: Credit = creditRepository.save(buildCreditDto(numberOfInstallment = 20).toEntity())
        val credit2: Credit = creditRepository.save(buildCreditDto(creditValue = BigDecimal(2000)).toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("${CreditResourceTest.URL}?customerId=1").accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditCode").value(credit1.creditCode.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(5000))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfInstallments").value(20))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].creditCode").value(credit2.creditCode.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].creditValue").value(2000))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].numberOfInstallments").value(15))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find any credit for the customer and return 200 status`() {
        //given
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("${CreditResourceTest.URL}?customerId=999")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find credit by credit code and customerId and return 200 status`() {
        //given
        val credit: Credit = creditRepository.save(buildCreditDto().toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("${CreditResourceTest.URL}/${credit.creditCode}?customerId=1")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(5000))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallment").value(15))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("mail@mail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(1000))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find credit by credit code and customerId and return 400 status`(){
        //given
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("${CreditResourceTest.URL}/${UUID.randomUUID()}?customerId=1")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request, see documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class com.api.ianloops.credit.application.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test


    /*@Test
    fun `should not find credit by credit code for this customerId and return 400 status`(){
        //given
        val credit: Credit = creditRepository.save(buildCreditDto().toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("${CreditResourceTest.URL}/${credit.creditCode}?customerId=999")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Contact Admin"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class java.lang.IllegalArgumentException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }*/


    private fun buildCreditDto(
        creditValue: BigDecimal = BigDecimal(5000),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusDays(3),
        numberOfInstallment: Int = 15,
        customerId: Long = 1L
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstOfInstallment,
        numberOfInstallment = numberOfInstallment,
        customerId = customerId
    )

}