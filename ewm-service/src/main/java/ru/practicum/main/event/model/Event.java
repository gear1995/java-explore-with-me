package ru.practicum.main.event.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String annotation;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @JoinColumn(name = "confirmed_requests")
    private Long confirmedRequests;

    @JoinColumn(name = "created_on")
    private LocalDateTime createdOn;

    private String description;

    @JoinColumn(name = "event_date")
    private LocalDateTime eventDate;

    @OneToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    private Boolean paid;

    @JoinColumn(name = "participant_limit")
    private Long participantLimit;

    @JoinColumn(name = "published_on")
    private LocalDateTime publishedOn;

    @JoinColumn(name = "request_moderation")

    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;
    private String title;
    private Long views;
}
