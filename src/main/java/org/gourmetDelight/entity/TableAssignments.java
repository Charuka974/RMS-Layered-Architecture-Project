package org.gourmetDelight.entity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class TableAssignments {
    private String tableID;
    private String reservationId;
    private LocalDateTime assignDateTime;
}
