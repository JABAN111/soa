package soa.study.agency_service.jpa.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("flat_stat")
public class FlatStat {

    @PrimaryKey
    private UUID id;

    @Column("flat_id")
    private Long flatId;

    @Column("number_of_rooms")
    private Long numberOfRooms;

    @Column("price")
    private Float price;

    @Column("created_at")
    private Long created_at;

    public FlatStat(Long flatId, Long numberOfRooms, Float price) {
        this.flatId = flatId;
        this.numberOfRooms = numberOfRooms;
        this.price = price;
        this.id = UUID.randomUUID();
        this.created_at = System.currentTimeMillis();
    }
}