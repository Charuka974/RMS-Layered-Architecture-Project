package org.gourmetDelight.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Payments {
    private String paymentID;
    private String paymentMethod;
    private double amount;
    private LocalDate paymentDate;

}
