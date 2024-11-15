package io.github.venkat1701.yugaantar.controllers;

import io.github.venkat1701.yugaantar.commons.controllers.GenericCrudController;
import io.github.venkat1701.yugaantar.dtos.registrations.RegistrationDTO;
import io.github.venkat1701.yugaantar.mappers.registrations.RegistrationMapper;
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

    public RegistrationController(
            RegistrationServiceImplementation registrationService,
            RegistrationMapper registrationMapper,
            PaymentServiceImplementation paymentService) {
        super(registrationService, registrationMapper);
        this.registrationService = registrationService;
        this.paymentService = paymentService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerForEvent(@Valid @RequestBody RegistrationDTO registrationDTO) {
        try {
            String paymentLink = registrationService.registerUserForEvent(registrationDTO);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Registration initiated successfully");
            response.put("paymentLink", paymentLink);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid registration request: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IllegalStateException e) {
            log.error("Registration state error: {}", e.getMessage());
            return createErrorResponse(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            log.error("Error during registration", e);
            return createErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred during registration"
            );
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<Map<String, String>> handlePaymentWebhook(
            @RequestHeader("X-Razorpay-Signature") String razorpaySignature,
            @NotBlank @RequestParam("payment_id") String paymentId) {
        try {
            Registration registration = registrationService.handlePaymentSuccess(paymentId);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payment processed successfully");
            response.put("registrationId", registration.getId().toString());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment webhook data: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Error processing payment webhook", e);
            return createErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to process payment webhook"
            );
        }
    }

    @GetMapping("/verify-payment/{paymentId}")
    public ResponseEntity<Map<String, String>> verifyPayment(@PathVariable String paymentId) {
        try {
            paymentService.verifyAndUpdatePayment(paymentId, null);
            String paymentLink = paymentService.getPaymentLink(paymentId);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("paymentLink", paymentLink);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment verification request: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Error during payment verification", e);
            return createErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to verify payment"
            );
        }
    }

    @GetMapping("/payment-status/{registrationId}")
    public ResponseEntity<Map<String, String>> getPaymentStatus(@PathVariable Long registrationId) {
        try {
            Registration registration = registrationService.findById(registrationId)
                    .orElseThrow(() -> new IllegalArgumentException("Registration not found"));

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("paymentStatus", registration.getPayment().getPaymentStatus().toString());
            response.put("transactionId", registration.getPayment().getTransactionId());

            return ResponseEntity.ok(response);
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

    private ResponseEntity<Map<String, String>> createErrorResponse(HttpStatus status, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}