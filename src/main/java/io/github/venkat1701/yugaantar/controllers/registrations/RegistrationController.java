package io.github.venkat1701.yugaantar.controllers.registrations;

import io.github.venkat1701.yugaantar.commons.controllers.GenericCrudController;
import io.github.venkat1701.yugaantar.dtos.payments.PaymentDTO;
import io.github.venkat1701.yugaantar.dtos.registrations.RegistrationDTO;
import io.github.venkat1701.yugaantar.exceptions.payments.PaymentFailedException;
import io.github.venkat1701.yugaantar.exceptions.registrations.RegistrationException;
import io.github.venkat1701.yugaantar.mappers.registrations.RegistrationMapper;
import io.github.venkat1701.yugaantar.models.payments.PaymentStatus;
import io.github.venkat1701.yugaantar.models.registrations.Registration;
import io.github.venkat1701.yugaantar.services.implementation.payments.PaymentServiceImplementation;
import io.github.venkat1701.yugaantar.services.implementation.registrations.RegistrationServiceImplementation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/registerevent")
public class RegistrationController extends GenericCrudController<Registration, RegistrationDTO, Long> {

    private final RegistrationServiceImplementation registrationService;
    private final PaymentServiceImplementation paymentService;

    public RegistrationController(RegistrationServiceImplementation registrationService, RegistrationMapper registrationMapper, PaymentServiceImplementation paymentService) {
        super(registrationService, registrationMapper);
        this.registrationService = registrationService;
        this.paymentService = paymentService;
    }

    @PostMapping("/register")
    public ResponseEntity<PaymentDTO> registerForEvent(@Valid @RequestBody RegistrationDTO registrationDTO) {
        try {
            String transID = registrationService.registerUserForEvent(registrationDTO);
            String paymentLink = paymentService.getPaymentLink(transID);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Registration initiated successfully");
            response.put("paymentLink", paymentLink);

            PaymentDTO paymentDTO = new PaymentDTO(
                    "SUCCESS",
                    "Registration Initiated Successfully",
                    paymentLink
            );

            return ResponseEntity.ok(paymentDTO);
        } catch (RegistrationException e) {
            log.error("Invalid registration request: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Error during registration", e);
            return createErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred during registration"
            );
        }
    }

    @GetMapping("/verify-payment/{paymentId}")
    public ResponseEntity<PaymentDTO> verifyPayment(@PathVariable String paymentId) {
        try {
            var payment = paymentService.verifyAndUpdatePayment(paymentId, null);
            String paymentLink = paymentService.getPaymentLink(paymentId);
            this.registrationService.updateUserRole(payment);
            return ResponseEntity.ok(new PaymentDTO("SUCCESS", "Verify Payment", paymentLink));
        } catch (Exception e) {
            log.error("Error during payment verification", e);
            return createErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to verify payment"
            );
        }
    }

    @GetMapping("/payment-status/{registrationId}")
    public ResponseEntity<PaymentDTO> getPaymentStatus(@PathVariable Long registrationId) {
        try {
            Registration registration = registrationService.findById(registrationId)
                    .orElseThrow(() -> new IllegalArgumentException("Registration not found"));

            return ResponseEntity.ok(new PaymentDTO("SUCCESS", registration.getPayment().getPaymentStatus().toString(), registration.getPayment().getTransactionId()));
        } catch (IllegalArgumentException e) {
            log.error("Invalid registration ID: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Error fetching payment status", e);
            return createErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch payment status"
            );
        }
    }

    private ResponseEntity<PaymentDTO> createErrorResponse(HttpStatus status, String message) {
        var response = new PaymentDTO(
          "ERROR",
                message,
                "https://"
        );
        return ResponseEntity.status(status).body(response);
    }
}