package com.pailsom.flare.batch.repository;

import com.pailsom.flare.batch.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
}
