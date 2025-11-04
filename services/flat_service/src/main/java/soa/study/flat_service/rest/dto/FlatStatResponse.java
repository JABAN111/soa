package soa.study.flat_service.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class FlatStatResponse {
    private Long id;

    private Long flatId;

    private Integer numberOfRooms;

    private Float price;


    public FlatStatResponse(Long flatid, Integer numberOfRooms) {
        this.flatId = flatid;
        this.numberOfRooms = numberOfRooms;
        this.price = this.numberOfRooms * 9000.0f;
    }
}
