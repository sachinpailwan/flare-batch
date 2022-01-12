package com.pailsom.flare.batch.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="employee")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "T_EMPLOYEE_ENRICHED")
public class EmployeeEnriched {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_EMPLOYEE_ENRICHED")
    @SequenceGenerator(sequenceName = "SEQ_EMPLOYEE_ENRICHED", allocationSize = 1, name = "SEQ_EMPLOYEE_ENRICHED")
    private long Id;

    @XmlElement(name = "firstName")
    private String firstName;
    @XmlElement(name = "lastName")
    private String lastName;
    @XmlElement(name = "departmentName")
    private String departmentName;
    @XmlElement(name = "departmentHead")
    private String departmentHead;
}
