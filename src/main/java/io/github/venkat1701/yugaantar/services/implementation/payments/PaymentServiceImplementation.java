package io.github.venkat1701.yugaantar.services.implementation.payments;

import com.razorpay.Invoice;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import io.github.venkat1701.yugaantar.dtos.registrations.RegistrationDTO;
import io.github.venkat1701.yugaantar.exceptions.payments.PaymentFailedException;
import io.github.venkat1701.yugaantar.models.entryTicket.EntryTicket;
import io.github.venkat1701.yugaantar.models.events.Event;
import io.github.venkat1701.yugaantar.models.payments.Payment;
import io.github.venkat1701.yugaantar.models.payments.PaymentStatus;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.repositories.payments.PaymentRepository;
import io.github.venkat1701.yugaantar.services.core.payments.PaymentGateway;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImplementation implements PaymentGateway {
    private final RazorpayClient razorpayClient;
    private final PaymentRepository paymentRepository;

    public PaymentServiceImplementation(
            PaymentRepository paymentRepository,
            @Value("${razorpay.api.key}") String razorpayKeyId,
            @Value("${razorpay.api.secret}") String razorpayKeySecret
    ) throws RazorpayException {
        this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Payment> findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    @Transactional
    public Payment updatePaymentStatus(Payment payment, PaymentStatus newStatus) {
        payment.setPaymentStatus(newStatus);
        if (newStatus == PaymentStatus.SUCCESS) {
            payment.setPaymentDate(new Date());
        }
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment createPayment(User user, Event event, int amount, String paymentMethod, String transactionId) {
        try {
            // Create Razorpay invoice options
            JSONObject invoiceOptions = createInvoiceOptions(user, event, amount, transactionId);
            // Create the invoice on Razorpay
            Invoice invoice = razorpayClient.invoices.create(invoiceOptions);

            // Create and save the Payment entity
            return createAndSavePayment(user, event, amount, paymentMethod, invoice);
        } catch (RazorpayException e) {
            throw new RuntimeException("Invoice creation failed: " + e.getMessage(), e);
        }
    }

    private JSONObject createInvoiceOptions(User user, Event event, int amount, String transactionId) {
        // Prepare customer details for Razorpay
        JSONObject customer = new JSONObject()
                .put("name", user.getUserProfile().getFirstName())
                .put("email", user.getEmail())
                .put("contact", user.getUserProfile().getPhoneNumber());

        JSONObject lineItem = new JSONObject()
                .put("name", event.getName())
                .put("amount", amount * 100)
                .put("currency", "INR")
                .put("quantity", 1);

        JSONObject notes = new JSONObject()
                .put("event_id", event.getId())
                .put("transaction_id", transactionId);

        return new JSONObject()
                .put("type", "invoice")
                .put("description", "Payment for " + event.getName())
                .put("customer", customer)
                .put("line_items", new JSONArray().put(lineItem))
                .put("notes", notes)
                .put("sms_notify", 1)
                .put("email_notify", 1)
                .put("currency", "INR")
                .put("expire_by", System.currentTimeMillis()/1000 + 86400);
    }

    private Payment createAndSavePayment(User user, Event event, int amount, String paymentMethod, Invoice invoice) {
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setEvent(event);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId(invoice.get("id"));
        payment.setPaymentStatus(PaymentStatus.INITIATED);
        payment.setOrderData(invoice.toString());
        payment.setCreatedAt(LocalDateTime.now());

        return savePayment(payment);
    }

    @Transactional
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment verifyAndUpdatePayment(String transactionId, String razorpaySignature) {
        try {
            Payment payment = findByTransactionId(transactionId)
                    .orElseThrow(() -> new PaymentFailedException("Payment not found"));

            Invoice invoice = razorpayClient.invoices.fetch(transactionId);
            String status = invoice.get("status");

            PaymentStatus newStatus = switch (status.toLowerCase()) {
                case "paid" -> PaymentStatus.SUCCESS;
                case "expired" -> PaymentStatus.FAILURE;
                default -> payment.getPaymentStatus();
            };

            return updatePaymentStatus(payment, newStatus);
        } catch (RazorpayException | PaymentFailedException e) {
            throw new RuntimeException("Payment verification failed: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public String getPaymentLink(String transactionId) {
        try {
            // Fetch the Razorpay invoice and return the payment link
            Invoice invoice = razorpayClient.invoices.fetch(transactionId);
            return invoice.get("short_url").toString();
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to fetch payment link: " + e.getMessage(), e);
        }
    }

    @Override
    public String createPaymentLink(RegistrationDTO registrationDTO) {
        try {
            JSONObject invoiceOptions = createInvoiceOptionsFromDTO(registrationDTO);

            Invoice invoice = razorpayClient.invoices.create(invoiceOptions);

            return invoice.get("short_url").toString();
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create payment link: " + e.getMessage(), e);
        }
    }

    private JSONObject createInvoiceOptionsFromDTO(RegistrationDTO registrationDTO) {
        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        Event event = new Event();
        int amount = registrationDTO.getAmountPaid();

        JSONObject customer = new JSONObject()
                .put("name", user.getUserProfile().getFirstName())
                .put("email", user.getEmail())
                .put("contact", user.getUserProfile().getPhoneNumber());

        JSONObject lineItem = new JSONObject()
                .put("name", event.getName())
                .put("amount", amount * 100)
                .put("currency", "INR")
                .put("quantity", 1);

        JSONObject notes = new JSONObject()
                .put("event_id", event.getId())
                .put("registration_id", registrationDTO.getEventID());

        return new JSONObject()
                .put("type", "invoice")
                .put("description", "Payment for " + event.getName())
                .put("customer", customer)
                .put("line_items", new JSONArray().put(lineItem))
                .put("notes", notes)
                .put("sms_notify", 1)
                .put("email_notify", 1)
                .put("currency", "INR")
                .put("expire_by", System.currentTimeMillis() / 1000 + 86400);
    }

    @Override
    public PaymentStatus getPaymentStatus(String paymentId, String transactionId) {
        try {
            Invoice invoice = razorpayClient.invoices.fetch(transactionId);
            String status = invoice.get("status").toString();

            switch (status.toLowerCase()) {
                case "paid":
                    return PaymentStatus.SUCCESS;
                case "expired":
                case "failed":
                    return PaymentStatus.FAILURE;
                default:
                    return PaymentStatus.PENDING;
            }
        } catch (RazorpayException e) {
            e.printStackTrace();
            return PaymentStatus.FAILURE;
        }
    }
}
