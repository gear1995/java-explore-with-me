package ru.practicum.main.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.main.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByEvent(Long eventId);

    @Query("SELECT req FROM Request AS req " +
           "JOIN Event AS e ON req.event = e.id " +
           "WHERE req.event = :eventId AND e.initiator.id = :userId")
    List<Request> findAllByEventWithInitiator(@Param(value = "userId") Long userId,
                                              @Param("eventId") Long eventId);

    List<Request> findAllByRequester(Long userId);

    Optional<Request> findByEventAndRequester(Long userId, Long eventId);
}
