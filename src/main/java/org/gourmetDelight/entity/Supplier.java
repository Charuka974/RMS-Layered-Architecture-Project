package org.gourmetDelight.entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Supplier {
    private String supplierID;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String userID;

}
