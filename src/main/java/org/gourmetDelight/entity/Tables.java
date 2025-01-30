package org.gourmetDelight.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Tables {
    private String tableID;
    private String tableNumber;
    private int capacity;
    private String location;
    private String status;
}
