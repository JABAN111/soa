package soa.study.flat_service.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import soa.study.flat_service.jpa.domain.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlatDto {
    private Integer id;

    private String name;

    private Coordinates coordinates;

    private LocalDate creationDate;

    private Integer area;

    private Integer numberOfRooms;

    private Integer numberOfBathrooms;

    private Furnish furnish;

    private Transport transport;

    private House house;

    public FlatDto(Flat flat) {
        this.id = flat.getId();
        this.name = flat.getName();
        this.coordinates = flat.getCoordinates();
        this.creationDate = flat.getCreationDate();
        this.area = flat.getArea();
        this.numberOfRooms = flat.getNumberOfRooms();
        this.numberOfBathrooms = flat.getNumberOfBathrooms();
        this.furnish = flat.getFurnish();
        this.transport = flat.getTransport();
        this.house = flat.getHouse();
    }

    public Flat toEntity() {
        var fl = new Flat();
        fl.setId(this.id);
        fl.setName(this.name);
        fl.setCoordinates(this.coordinates);
        fl.setCreationDate(this.creationDate);
        fl.setArea(this.area);
        fl.setNumberOfRooms(this.numberOfRooms);
        fl.setNumberOfBathrooms(this.numberOfBathrooms);
        fl.setFurnish(this.furnish);
        fl.setTransport(this.transport);
        fl.setHouse(this.house);
        return fl;
    }
}
