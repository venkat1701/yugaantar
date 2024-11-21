package io.github.venkat1701.yugaantar.models.entryTicket;

import io.github.venkat1701.yugaantar.models.payments.PaymentStatus;
import io.github.venkat1701.yugaantar.models.users.User;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
public class EntryTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID entryId;

    @ManyToOne
    private User user;
    private String transactionId;
    private PaymentStatus paymentStatus; // PENDING, PAID, LOGGED-IN
    private Date createdAt;

    public UUID getEntryId() {
        return entryId;
    }

    public void setEntryId(UUID entryId) {
        this.entryId = entryId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
