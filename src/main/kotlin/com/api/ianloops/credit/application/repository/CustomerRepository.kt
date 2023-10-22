package com.api.ianloops.credit.application.repository;

import com.api.ianloops.credit.application.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<Customer, Long>