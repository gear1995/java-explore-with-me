package ru.practicum.main.event.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Table(name = "locations")
public class Location {
    @Id
    private Long id;
    private float lat;
    private float lon;
}
