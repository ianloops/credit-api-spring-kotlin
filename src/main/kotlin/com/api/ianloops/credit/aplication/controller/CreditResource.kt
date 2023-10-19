package com.api.ianloops.credit.aplication.controller

import com.api.ianloops.credit.aplication.dto.CreditDto
import com.api.ianloops.credit.aplication.dto.CreditView
import com.api.ianloops.credit.aplication.dto.CreditViewList
import com.api.ianloops.credit.aplication.model.Credit
import com.api.ianloops.credit.aplication.service.impl.CreditService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/credits")
class CreditResource(private val creditService: CreditService) {

    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<String> {
        val credit: Credit = this.creditService.save(creditDto.toEntity())
        val message: String = "Credit ${credit.creditCode} - Customer ${credit.customer?.firstName} saved!"
        return ResponseEntity.status(HttpStatus.CREATED).body(message)
    }

    @GetMapping
    fun findAllCustomerCredits(
        @RequestParam(value = "customerId") customerId: Long
    ): ResponseEntity<List<CreditViewList>> {
        val creditViewList = this.creditService.findAllByCustomer(customerId)
            .stream().map { credit: Credit -> CreditViewList(credit) }
            .collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(creditViewList)
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(
        @RequestParam(value = "customerId")
        customerId: Long,

        @PathVariable creditCode: UUID
    ): ResponseEntity<CreditView> {
        val credit = this.creditService.findByCreditCode(customerId, creditCode)
        return ResponseEntity.status(HttpStatus.OK).body(CreditView(credit))
    }
}