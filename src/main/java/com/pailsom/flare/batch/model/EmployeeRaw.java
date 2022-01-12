package com.pailsom.flare.batch.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_EMPLOYEE_RAW")
public class EmployeeRaw {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_EMPLOYEE_RAW")
    @SequenceGenerator(sequenceName = "SEQ_EMPLOYEE_RAW", allocationSize = 1, name = "SEQ_EMPLOYEE_RAW")
    private long Id;

    private String firstName;
    private String lastName;
    private long departmentId;
}
