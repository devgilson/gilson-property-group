package com.example.breken30daysback.Repository;

import com.example.breken30daysback.Entity.Financials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialsRepository extends JpaRepository<Financials, String> {
}