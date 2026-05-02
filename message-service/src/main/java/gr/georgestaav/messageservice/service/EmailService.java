package gr.georgestaav.messageservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public boolean sendWelcomeEmail(String to, Long membershipNumber) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Καλώς ήρθατε στο ΕΑΚ Swimming!");
            message.setText("""
                    Γεια σας!
                    
                    Η εγγραφή σας ολοκληρώθηκε με επιτυχία. 
                    Ο αριθμός μέλους σας είναι: %s.
                    
                    Σας περιμένουμε στην πισίνα!
                    
                    ΕΑΚ Swimming
                    """.formatted(membershipNumber));

            mailSender.send(message);
            log.info("Welcome email sent to: {}", to);
            return true;
        }catch (Exception e) {
            log.error("Failed to send welcome email to: {}", to, e);
            return false;
        }
    }

    public void sendExpiryWarning(String to, String name, LocalDate endDate) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Η συνδρομή σας λήγει σύντομα!");
            message.setText("""
                    %s,
                    
                     Η συνδρομή σας στο ΕΑΚ Swimming λήγει στις %s.
                     Παρακαλούμε επισκεφτείτε τη γραμματεία για ανανέωση.
                    
                     ΕΑΚ Swimming
                    """.formatted(name, endDate));

            mailSender.send(message);
            log.info("Expiry warning email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send expiry warning email to: {}", to, e);
        }
    }

    public void sendExpiredMessage(String to, String name, LocalDate endDate) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Η συνδρομή σας έχει λήξει");
            message.setText("""
                    %s,
                    
                    Η συνδρομή σας στο ΕΑΚ Swimming έληξε στις %s.
                    Παρακαλούμε επισκεφτείτε τη γραμματεία για ανανέωση.
                    
                    ΕΑΚ Swimming
                    """.formatted(name, endDate));

            mailSender.send(message);
            log.info("Expired notification email sent to: {}", to);
        }catch (Exception e) {
            log.error("Failed to send expired email to: {}", to, e);
        }
    }

}
