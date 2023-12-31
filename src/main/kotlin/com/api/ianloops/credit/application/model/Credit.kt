package com.api.ianloops.credit.application.model

import com.api.ianloops.credit.application.ennumeration.Status
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
data class Credit(
    @Column(nullable = false, unique = true)
    var creditCode: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val creditValue: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    val dayFirstInstallment: LocalDate,

    @Column(nullable = false)
    val numberOfInstallment: Int = 0,

    @Enumerated val status: Status = Status.IN_PROGRESS,

    @ManyToOne var customer: Customer? = null,

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id val id: Long? = null
)