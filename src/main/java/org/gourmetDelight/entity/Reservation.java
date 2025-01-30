package org.gourmetDelight.entity;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Reservation {

    private String reservationID;
    private String customerID;
    private LocalDate reservationDate;
    private int numberOfGuests;
    private String specialRequests;
    private String status;
}
