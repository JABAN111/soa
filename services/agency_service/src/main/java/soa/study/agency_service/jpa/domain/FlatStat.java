package soa.study.agency_service.jpa.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
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
    private Integer numberOfRooms;

    @Column("price")
    private Float price;

    private Timestamp created_at;

    public FlatStat(Long flatId, Integer numberOfRooms, Float price) {
        this.flatId = flatId;
        this.numberOfRooms = numberOfRooms;
        this.price = price;
        this.id = UUID.randomUUID();
        this.created_at = new Timestamp(System.currentTimeMillis());
    }
}