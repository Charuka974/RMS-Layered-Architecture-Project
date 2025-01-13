package org.gourmetDelight.dto.employee;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class UserDto {
    private String userId;
    private String username;
    private String password;
    private LocalDate loginTime;
    private String employeeID;
}
