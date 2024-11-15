package io.github.venkat1701.yugaantar.repositories.registrations;

import io.github.venkat1701.yugaantar.models.registrations.Registration;
import io.github.venkat1701.yugaantar.models.events.Event;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.models.payments.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // User related queries
    List<Registration> findByUser(User user);
    List<Registration> findByUserOrderByCreatedAtDesc(User user);
    Optional<Registration> findByUserAndEvent(User user, Event event);
    boolean existsByUserAndEvent(User user, Event event);

    // Event related queries
    List<Registration> findByEvent(Event event);
    List<Registration> findByEventOrderByCreatedAtDesc(Event event);

    // Status related queries
    List<Registration> findByRegistrationStatus(String status);
    List<Registration> findByRegistrationStatusAndEvent(String status, Event event);
    List<Registration> findByRegistrationStatusAndUser(String status, User user);

    // Payment related queries
    Optional<Registration> findByPayment(Payment payment);
    List<Registration> findByAmountPaidGreaterThan(int amount);
    List<Registration> findByAmountPaidLessThan(int amount);

    // Date related queries
    List<Registration> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Registration> findByRegistrationDate(String registrationDate);

    // Custom queries
    @Query("SELECT r FROM Registration r WHERE r.event = :event AND r.registrationStatus = 'CONFIRMED'")
    List<Registration> findConfirmedRegistrationsForEvent(@Param("event") Event event);

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.event = :event AND r.registrationStatus = 'CONFIRMED'")
    long countConfirmedRegistrationsForEvent(@Param("event") Event event);

    @Query("SELECT r FROM Registration r WHERE r.user = :user AND r.createdAt >= :startDate")
    List<Registration> findRecentRegistrationsByUser(@Param("user") User user, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT DISTINCT r.registrationStatus FROM Registration r")
    List<String> findAllDistinctStatuses();

    // Combined queries
    List<Registration> findByEventAndCreatedAtBetween(Event event, LocalDateTime startDate, LocalDateTime endDate);
    List<Registration> findByUserAndRegistrationStatusAndCreatedAtBetween(
            User user,
            String status,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}