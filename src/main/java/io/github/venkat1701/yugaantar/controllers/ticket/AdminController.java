package io.github.venkat1701.yugaantar.controllers.ticket;

import io.github.venkat1701.yugaantar.exceptions.entry.EntryTicketNotFoundException;
import io.github.venkat1701.yugaantar.models.entryTicket.EntryTicket;
import io.github.venkat1701.yugaantar.models.payments.PaymentStatus;
import io.github.venkat1701.yugaantar.services.implementation.payments.EntryTicketService;
import io.github.venkat1701.yugaantar.services.implementation.qrCode.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final EntryTicketService entryTicketService;
    private final QRCodeService qrCodeService;

    @Autowired
    public AdminController(EntryTicketService entryTicketService, QRCodeService qrCodeService) {
        this.entryTicketService = entryTicketService;
        this.qrCodeService = qrCodeService;
    }

    /**
     * Endpoint to scan a QR code and update the EntryTicket status to LOGGED-IN.
     *
     * @param entryId The ID of the EntryTicket obtained from the QR code.
     * @return Status update response.
     */
    @PutMapping("/scan")
    public ResponseEntity<String> scanEntryTicket(@RequestParam UUID entryId) {
        try {
            // Fetch the EntryTicket by entryId
            EntryTicket ticket = entryTicketService.getById(entryId)
                    .orElseThrow(() -> new EntryTicketNotFoundException("EntryTicket with ID " + entryId + " not found"));

            // Check if payment is successful
            if (ticket.getPaymentStatus() != PaymentStatus.SUCCESS) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment not verified for this ticket");
            }

            // Check if already logged in
            if (ticket.getPaymentStatus() == PaymentStatus.LOGGED_IN) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User already logged in");
            }

            // Update paymentStatus to LOGGED_IN
            ticket.setPaymentStatus(PaymentStatus.LOGGED_IN);
            entryTicketService.save(ticket); // Save the updated ticket

            return ResponseEntity.ok("Entry Status Updated to LOGGED-IN");

        } catch (EntryTicketNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + ex.getMessage());  // Return 404 Not Found if EntryTicket is not found
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + ex.getMessage());  // Handle any other unexpected errors
        }
    }
}
