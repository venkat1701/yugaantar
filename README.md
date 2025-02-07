
# **Entry Gate Ticket System**

## **Overview**

This system implements the **Entry Gate Ticket** functionality for an event, enabling:
1. **User Workflow**:
   - Users can purchase tickets, generate a payment link, and complete the payment.
   - After successful payment, a QR code is generated and linked to the user's profile.
2. **Admin Workflow**:
   - Admins can scan QR codes to validate tickets and mark users as "LOGGED-IN."

---

## **Workflow**

### **User Purchase Workflow**
1. **Ticket Purchase**:
   - User pre-registers and requests a ticket.
   - Backend generates a Razorpay payment link (`createPaymentLink`).
2. **Payment**:
   - User completes the payment using the link.
   - Payment status is verified (`getPaymentStatus`).
3. **QR Code Generation**:
   - On successful payment, a QR code is generated.
   - The QR code is linked to the user's profile.

### **Admin Scanning Workflow**
1. Admin scans the QR code.
2. Backend retrieves ticket details based on the QR code.
3. If the payment is successful, the user is marked as "LOGGED-IN."

---

## **Project Structure**

### **1. Models**

1. **`EntryTicket`**
   - Represents a ticket purchased by the user.
   - **Key Fields**:
     - `transactionId`: Unique transaction identifier.
     - `paymentStatus`: Status of the payment (e.g., INITIATED, SUCCESS, FAILURE).
     - `createdAt` and `updatedAt`: Timestamps for tracking.

2. **`Payment`**
   - Represents payment details for a transaction.
   - **Key Fields**:
     - `transactionId`: Unique identifier for the payment.
     - `amount`: Payment amount.
     - `paymentStatus`: Status of the payment (e.g., SUCCESS, FAILURE).
     - `createdAt`: Timestamp for when the payment was initiated.

3. **`UserProfile`**
   - Enhanced to include a field for QR code storage.
   - **Key Fields**:
     - `qrCode`: A binary large object (`byte[]`) to store the QR code.
     - User details such as `firstName`, `lastName`, and `phoneNumber`.

---

### **2. Repositories**

1. **`EntryTicketRepository`**
   - Manages database operations for `EntryTicket`.
   - **Key Methods**:
     - `Optional<EntryTicket> findByTransactionId(String transactionId)`: Fetches a ticket by its transaction ID.

2. **`PaymentRepository`**
   - Handles persistence of `Payment` details.
   - **Key Methods**:
     - `Optional<Payment> findByTransactionId(String transactionId)`: Finds a payment by its transaction ID.

3. **`UserProfileRepository`**
   - Manages user profile data.
   - **Key Methods**:
     - `Optional<UserProfile> findByUserId(Long userId)`: Fetches a user profile by user ID.

---

### **3. Services**

#### **Payment Services**
1. **`PaymentGateway` (Interface)**
   - Methods:
     - `String createPaymentLink(RegistrationDTO registrationDTO)`: Generates a payment link.
     - `PaymentStatus getPaymentStatus(String paymentId, String transactionId)`: Verifies the payment status.

2. **`PaymentServiceImplementation`**
   - Implements `PaymentGateway`.
   - **Key Methods**:
     - `createPaymentLink`: Interacts with Razorpay to create payment links.
     - `getPaymentStatus`: Verifies the payment status using Razorpay.

#### **QR Code Services**
1. **`QRCodeService`**
   - Manages QR code generation and linking to user profiles.
   - **Key Methods**:
     - `generateAndSaveQRCode(Long userId, UUID entryId)`: Generates a QR code for a ticket and saves it in the user's profile.

#### **Entry Ticket Services**
1. **`EntryTicketService`**
   - Handles ticket creation, payment initiation, and status updates.
   - **Key Methods**:
     - `createAndInitiatePayment`: Creates a ticket and initiates the payment link process.
     - `verifyAndSetPaymentStatus`: Verifies payment status and updates the ticket accordingly.

---

### **4. Controllers**

#### **PaymentController**
- **Endpoints**:
  1. `POST /api/payments/create-link`:
     - Input: `RegistrationDTO`
     - Output: Payment link (e.g., `https://rzp.io/l/paymentlink`).
  2. `GET /api/payments/status`:
     - Input: `paymentId` and `transactionId`
     - Output: Payment status (e.g., `SUCCESS`, `PENDING`).

#### **TicketController**
- **Endpoints**:
  1. `POST /api/tickets/purchase`:
     - Initiates ticket purchase and generates a payment link.
  2. `POST /api/tickets/generate-qr`:
     - Input: `userId` and `entryId`.
     - Action: Generates and stores a QR code.
  3. `PUT /api/admin/scan`:
     - Input: `entryId`.
     - Action: Validates the QR code and marks the user as "LOGGED-IN."

---

## **5. Future Steps**
#### **Integration Testing:**

- Add Razorpay API keys in application.properties for live testing.
- Verify Razorpay payment link creation and status verification.
- Error Handling:
  Add detailed error messages for cases like payment failures, expired links, or invalid QR codes.
