package org.gourmetDelight.entity.custom;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ReservationCustom {

    private String reservationID;
    private String customerID;
    private LocalDate reservationDate;
    private int numberOfGuests;
    private String specialRequests;
    private String status;
    private String tableID;
}