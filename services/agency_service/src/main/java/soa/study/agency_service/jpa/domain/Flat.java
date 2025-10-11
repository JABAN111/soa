package soa.study.agency_service.jpa.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flats")
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    private String name;

    @Valid
    @NotNull(message = "Coordinates cannot be null")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "coordinate_x")),
            @AttributeOverride(name = "y", column = @Column(name = "coordinate_y"))
    })
    private Coordinates coordinates;

    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @NotNull(message = "Area cannot be null")
    @Min(value = 1, message = "Area must be greater than 0")
    @Column(nullable = false)
    private Integer area;

    @NotNull(message = "Number of rooms cannot be null")
    @Min(value = 1, message = "Number of rooms must be greater than 0")
    @Column(nullable = false)
    private Integer numberOfRooms;

    @NotNull(message = "Number of bathrooms cannot be null")
    @Min(value = 1, message = "Number of bathrooms must be greater than 0")
    @Column(nullable = false)
    private Integer numberOfBathrooms;

    @NotNull(message = "Furnish cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Furnish furnish;

    @Enumerated(EnumType.STRING)
    private Transport transport;

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "house_name")),
            @AttributeOverride(name = "year", column = @Column(name = "house_year")),
            @AttributeOverride(name = "numberOfFloors", column = @Column(name = "house_floors"))
    })
    private House house;

    @PrePersist
    protected void onCreate() {
        if (creationDate == null) {
            creationDate = LocalDate.now();
        }
    }
}