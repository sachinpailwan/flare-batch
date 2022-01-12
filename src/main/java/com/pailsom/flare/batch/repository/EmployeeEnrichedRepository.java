package com.pailsom.flare.batch.repository;

import com.pailsom.flare.batch.model.EmployeeEnriched;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeEnrichedRepository extends JpaRepository<EmployeeEnriched,Long> {
}
