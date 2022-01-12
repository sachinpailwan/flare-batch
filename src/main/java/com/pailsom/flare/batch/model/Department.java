package com.pailsom.flare.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_DEPARTMENT")
public class Department {

    @Id
    private long id;

    private String departmentName;

    private String headOfDepartment;

}
