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

    List<Registration> findByUser(User user);
    List<Registration> findByUserOrderByCreatedAtDesc(User user);
    Optional<Registration> findByUserAndEvent(User user, Event event);
    boolean existsByUserAndEvent(User user, Event event);

    List<Registration> findByEvent(Event event);
    List<Registration> findByEventOrderByCreatedAtDesc(Event event);

    List<Registration> findByRegistrationStatus(String status);
    List<Registration> findByRegistrationStatusAndEvent(String status, Event event);
    List<Registration> findByRegistrationStatusAndUser(String status, User user);

    Optional<Registration> findByPayment(Payment payment);
    List<Registration> findByAmountPaidGreaterThan(int amount);
    List<Registration> findByAmountPaidLessThan(int amount);
    List<Registration> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Registration> findByRegistrationDate(String registrationDate);

    @Query("SELECT r FROM Registration r WHERE r.event = :event AND r.registrationStatus = 'CONFIRMED'")
    List<Registration> findConfirmedRegistrationsForEvent(@Param("event") Event event);

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.event = :event AND r.registrationStatus = 'CONFIRMED'")
    long countConfirmedRegistrationsForEvent(@Param("event") Event event);

    @Query("SELECT r FROM Registration r WHERE r.user = :user AND r.createdAt >= :startDate")
    List<Registration> findRecentRegistrationsByUser(@Param("user") User user, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT DISTINCT r.registrationStatus FROM Registration r")
    List<String> findAllDistinctStatuses();

    List<Registration> findByEventAndCreatedAtBetween(Event event, LocalDateTime startDate, LocalDateTime endDate);
    List<Registration> findByUserAndRegistrationStatusAndCreatedAtBetween(
            User user,
            String status,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("SELECT r.user FROM Registration r " +
            "WHERE r.event = :event " +
            "AND r.registrationStatus = 'CONFIRMED' " +
            "AND r.payment IS NOT NULL " +
            "AND r.amountPaid > 0")
    List<User> findSuccessfullyRegisteredUsers(@Param("event") Event event);

    @Query("SELECT r FROM Registration r " +
            "WHERE r.event = :event " +
            "AND r.registrationStatus = 'CONFIRMED' " +
            "AND r.payment IS NOT NULL " +
            "AND r.amountPaid > 0 " +
            "ORDER BY r.createdAt DESC")
    List<Registration> findSuccessfulRegistrations(@Param("event") Event event);

    @Query("SELECT COUNT(r) > 0 FROM Registration r " +
            "WHERE r.event = :event " +
            "AND r.user = :user " +
            "AND r.registrationStatus = 'CONFIRMED' " +
            "AND r.payment IS NOT NULL " +
            "AND r.amountPaid > 0")
    boolean isUserSuccessfullyRegistered(@Param("event") Event event, @Param("user") User user);

    @Query("SELECT COUNT(r) FROM Registration r " +
            "WHERE r.event = :event " +
            "AND r.registrationStatus = 'CONFIRMED' " +
            "AND r.payment IS NOT NULL " +
            "AND r.amountPaid > 0")
    long countSuccessfulRegistrations(@Param("event") Event event);
}