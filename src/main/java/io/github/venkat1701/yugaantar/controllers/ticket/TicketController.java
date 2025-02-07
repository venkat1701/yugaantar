package io.github.venkat1701.yugaantar.controllers.ticket;

import io.github.venkat1701.yugaantar.models.events.Event;
import io.github.venkat1701.yugaantar.models.entryTicket.EntryTicket;
import io.github.venkat1701.yugaantar.services.implementation.qrCode.QRCodeService;
import io.github.venkat1701.yugaantar.services.implementation.payments.EntryTicketService;
import io.github.venkat1701.yugaantar.repositories.events.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final EntryTicketService entryTicketService;
    private final QRCodeService qrCodeService;
    private final EventRepository eventRepository;

    @Autowired
    public TicketController(EntryTicketService entryTicketService, QRCodeService qrCodeService, EventRepository eventRepository) {
        this.entryTicketService = entryTicketService;
        this.qrCodeService = qrCodeService;
        this.eventRepository = eventRepository;
    }

    /**
     * Endpoint to purchase an Entry Ticket.
     *
     * @param userId  The ID of the user purchasing the ticket.
     * @param eventId The ID of the event.
     * @param amount  The amount to be paid.
     * @return Payment link or relevant response.
     */
    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseEntryTicket(
            @RequestParam Long userId,
            @RequestParam Long eventId,
            @RequestParam int amount
    ) {
        // Fetch the event based on eventId
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Create and initiate payment
        EntryTicket ticket = entryTicketService.createAndInitiatePayment(userId, event, amount);

        // Fetch the actual payment link from Razorpay
        String paymentLink = entryTicketService.getPaymentLink(ticket.getTransactionId());  // Get actual link from Razorpay

        return ResponseEntity.ok("Payment Link Created: " + paymentLink);
    }

    /**
     * Endpoint to verify payment after user completes the transaction.
     *
     * @param transactionId      The transaction ID provided during ticket purchase.
     * @param razorpaySignature  The Razorpay signature to verify payment.
     * @return Verification status.
     */
    @GetMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(
            @RequestParam String transactionId,
            @RequestParam String razorpaySignature
    ) {
        try {
            // Verify payment status and update EntryTicket
            entryTicketService.verifyAndSetPaymentStatus(transactionId, razorpaySignature);
            return ResponseEntity.ok("Payment Verified Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment Verification Failed: " + e.getMessage());
        }
    }

    /**
     * Endpoint to generate and associate QR code after successful payment.
     *
     * @param userId  The ID of the user.
     * @param entryId The ID of the EntryTicket.
     * @return QR code generation status.
     */
    @PostMapping("/generate-qr")
    public ResponseEntity<String> generateQRCode(
            @RequestParam Long userId,
            @RequestParam UUID entryId
    ) {
        try {
            // Generate and save QR code for the user and entry ticket
            qrCodeService.generateAndSaveQRCode(userId, entryId);
            return ResponseEntity.ok("QR Code Generated and Associated with User Profile");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to Generate QR Code: " + e.getMessage());
        }
    }
}
