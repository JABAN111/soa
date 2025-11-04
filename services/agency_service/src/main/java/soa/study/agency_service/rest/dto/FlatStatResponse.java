package soa.study.agency_service.rest.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import soa.study.agency_service.jpa.domain.FlatStat;

@Data
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FlatStatResponse {
    private Long id;

    private Long flatId;

    private Integer numberOfRooms;

    private Float price;


    public FlatStatResponse(FlatStat flat) {
        this.flatId = flat.getFlatId();
        this.numberOfRooms = flat.getNumberOfRooms();
        if (flat.getPrice() != null) {
            this.price = this.numberOfRooms * 9000.0f;
        }
    }


    public FlatStat toFlatStat() {
        var stat = new FlatStat();
        stat.setFlatId(this.flatId);
        stat.setNumberOfRooms(this.numberOfRooms);
        if (this.price == null) {
            this.price = this.numberOfRooms * 9000f;
            stat.setPrice(this.price);
        }
        return stat;
    }
}
