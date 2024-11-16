package io.github.venkat1701.yugaantar.repositories.events;

import io.github.venkat1701.yugaantar.models.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findEventByName(String name);
    List<Event> findEventByLocation(String location);

    List<Event> findEventByVenueName(String venueName);
    List<Event> findEventByVenueId(Long venueId);

    List<Event> findEventByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Event> findEventByEndDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Event> findEventByStatus(String status);
    List<Event> findEventByEventType(String eventType);

    List<Event> findEventByTicketPriceLessThanEqual(int maxPrice);
    List<Event> findEventByTicketPriceBetween(int minPrice, int maxPrice);
    List<Event> findEventByIsFeatured(boolean featured);

    List<Event> findEventByCurrentParticipants(int currentParticipants);

    @Query("SELECT e FROM Event e WHERE " +
            "(:eventType IS NULL OR e.eventType = :eventType) AND " +
            "(:location IS NULL OR e.location = :location) AND " +
            "(:startDate IS NULL OR e.startDate >= :startDate) AND " +
            "(:maxPrice IS NULL OR e.ticketPrice <= :maxPrice) AND " +
            "e.currentParticipants < e.maxParticipants")
    List<Event> findEventsByFilters(
            @Param("eventType") String eventType,
            @Param("location") String location,
            @Param("startDate") LocalDateTime startDate,
            @Param("maxPrice") Integer maxPrice
    );

    long countByEventType(String eventType);
    long countByLocation(String location);

    @Query("SELECT COUNT(e) FROM Event e WHERE " +
            "e.startDate >= :startDate AND e.startDate <= :endDate")
    long countEventsBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}