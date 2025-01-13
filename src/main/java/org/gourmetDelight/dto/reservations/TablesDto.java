package org.gourmetDelight.dto.reservations;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class TablesDto {
    private String tableID;
    private String tableNumber;
    private int capacity;
    private String location;
    private String status;
}
