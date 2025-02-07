package io.github.venkat1701.yugaantar.repositories.entryTicket;

import io.github.venkat1701.yugaantar.models.entryTicket.EntryTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntryTicketRepository extends JpaRepository<EntryTicket, UUID> {
    Optional<EntryTicket> findByTransactionId(String transactionId);
}
