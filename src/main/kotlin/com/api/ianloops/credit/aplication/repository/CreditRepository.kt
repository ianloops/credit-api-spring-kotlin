package com.api.ianloops.credit.aplication.repository;

import com.api.ianloops.credit.aplication.model.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CreditRepository : JpaRepository<Credit, Long>