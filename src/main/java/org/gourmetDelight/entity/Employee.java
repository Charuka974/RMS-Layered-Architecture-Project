package org.gourmetDelight.entity;

import java.time.LocalDate;

import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Employee {
    private String employeeID;
    private String name;
    private String position;
    private String phone;
    private String email;
    private LocalDate hireDate;



}
