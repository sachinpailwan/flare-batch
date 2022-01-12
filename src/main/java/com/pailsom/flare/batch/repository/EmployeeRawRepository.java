package com.pailsom.flare.batch.repository;

import com.pailsom.flare.batch.model.EmployeeRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRawRepository extends JpaRepository<EmployeeRaw,Long> {
}
