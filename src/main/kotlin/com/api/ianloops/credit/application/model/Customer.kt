package com.api.ianloops.credit.application.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "Customers")
data class Customer(
    @Column(nullable = false)
    var firstName: String = "",

    @Column(nullable = false)
    var lastName: String = "",

    @Column(nullable = false, unique = true)
    var cpf: String = "",

    @Column(nullable = false)
    var income: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, unique = true)
    var email: String = "",

    @Column(nullable = false)
    var password: String = "",

    @Column(nullable = false)
    @Embedded var address: Address = Address(),

    @Column(nullable = false)
    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE, CascadeType.ALL],
        mappedBy = "customer"
    ) var credits: List<Credit> = mutableListOf(),

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id val id: Long? = null
)