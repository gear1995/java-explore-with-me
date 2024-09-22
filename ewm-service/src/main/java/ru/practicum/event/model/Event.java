package ru.practicum.event.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @JoinColumn(name = "confirmed_requests")
    private Long confirmedRequests;
    @JoinColumn(name = "created_on")
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    @OneToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @OneToOne
    private Location location;
    private Boolean paid;
    @JoinColumn(name = "participant_limit")
    private Long participantLimit;
    @JoinColumn(name = "published_on")
    private LocalDateTime publishedOn;
    @JoinColumn(name = "request_moderation")
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
}
