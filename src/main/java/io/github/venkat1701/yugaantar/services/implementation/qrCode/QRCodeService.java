package io.github.venkat1701.yugaantar.services.implementation.qrCode;

import io.github.venkat1701.yugaantar.services.implementation.users.UserProfileServiceImplementation;
import io.github.venkat1701.yugaantar.utilities.qrcode.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QRCodeService {

    private final UserProfileServiceImplementation userProfileService;

    @Autowired
    public QRCodeService(UserProfileServiceImplementation userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * Generates a QR code for the given EntryTicket and associates it with the user's profile.
     *
     * @param userId  The ID of the user.
     * @param entryId The ID of the EntryTicket.
     * @throws Exception If QR code generation fails.
     */
    public void generateAndSaveQRCode(Long userId, UUID entryId) throws Exception {
        String qrContent = entryId.toString(); // QR Code content
        byte[] qrCode = QRCodeGenerator.generateQRCodeImage(qrContent);

        // Associate the generated QR code with the user's profile
        userProfileService.associateQrCode(userId, qrCode);
    }

    /**
     * Retrieves the QR code for a given user ID.
     *
     * @param userId The ID of the user.
     * @return The QR code as a byte array.
     */
    public byte[] getQRCode(Long userId) {
        return userProfileService.getQrCode(userId);
    }
}
