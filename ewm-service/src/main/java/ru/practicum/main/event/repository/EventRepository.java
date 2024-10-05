package ru.practicum.main.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorId(Long userId, Pageable page);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    Optional<Event> findByIdAndPublishedOnIsNotNull(Long id);

    List<Event> findAllByIdIn(List<Long> events);

    boolean existsByCategoryId(long catId);


    @Query("SELECT e FROM Event AS e " +
           "WHERE ((:usersId) IS NULL OR e.initiator.id IN :usersId) " +
           "AND ((:states) IS NULL OR e.state IN :states) " +
           "AND ((:categoriesId) IS NULL OR e.category.id IN :categoriesId) " +
           "AND e.eventDate >= :rangeStart " +
           "AND e.eventDate <= :rangeEnd " +
           "ORDER BY e.id DESC ")
    List<Event> getAllUsersEvents(List<Long> usersId,
                                  List<Long> categoriesId,
                                  EventState states,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  Pageable pageable);

    @Query("SELECT e FROM Event e " +
           "WHERE e.annotation iLIKE :text " +
           "OR e.description iLIKE :text OR :text IS NULL " +
           "AND e.category.id IN :categories " +
           "AND e.paid = :paid " +
           "AND e.eventDate > :rangeStart " +
           "AND e.eventDate < :rangeEnd")
    List<Event> getFilterEvents(String text,
                                List<Long> categories,
                                Boolean paid,
                                LocalDateTime rangeStart,
                                LocalDateTime rangeEnd,
                                Pageable pageable);
}
