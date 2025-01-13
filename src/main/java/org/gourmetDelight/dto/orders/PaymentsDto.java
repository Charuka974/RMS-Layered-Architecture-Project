package org.gourmetDelight.dto.orders;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class PaymentsDto {
    private String paymentID;
    private String paymentMethod;
    private double amount;
    private LocalDate paymentDate;

}
