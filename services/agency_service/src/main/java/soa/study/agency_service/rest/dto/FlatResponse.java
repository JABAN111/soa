package soa.study.agency_service.rest.dto;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import soa.study.agency_service.jpa.domain.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FlatResponse {
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

    private Float price;


    public FlatResponse(Flat flat) {
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
        this.price = this.numberOfRooms * 9000.0f;
    }
}
