package soa.study.agency_service.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import soa.study.agency_service.jpa.domain.*;

@Data
@NoArgsConstructor
public class FlatStatResponse {
    private Long id;

    private Long flatId;

    private Integer numberOfRooms;

    private Float price;


    public FlatStatResponse(FlatStat flat) {
        this.flatId = flat.getFlatId();
        this.numberOfRooms = flat.getNumberOfRooms();
        this.price = this.numberOfRooms * 9000.0f;
    }
}
