package com.example.todo.services.implementations;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.example.todo.exceptions.SendingEmailException;
import com.example.todo.services.interfaces.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class EmailService implements IEmailService {

        private static final String CHAR_SET = "UTF-8";
        private static String SUBJECT = null;
        private final AmazonSimpleEmailService emailService;
        private final String sender;

        @Autowired
        public EmailService(AmazonSimpleEmailService emailService, @Value("${email.sender}") String sender) {
                this.emailService = emailService;
                this.sender = sender;
        }

        public void sendEmail(String emailType, String email, String token) {

                try {
                        String body = prepareEmailBody(emailType, token);

                        int requestTimeout = 3000;
                        SendEmailRequest request = new SendEmailRequest()
                                        .withDestination(new Destination().withToAddresses(email))
                                        .withMessage(new Message()
                                                        .withBody(new Body().withHtml(new Content()
                                                                        .withCharset(CHAR_SET).withData(body)))
                                                        .withSubject(new Content().withCharset(CHAR_SET)
                                                                        .withData(SUBJECT)))
                                        .withSource(sender).withSdkRequestTimeout(requestTimeout);
                        emailService.sendEmail(request);
                } catch (Exception ex) {
                        log.error(ex.getMessage());
                        throw new SendingEmailException(
                                        String.format("%s", "Sending email failed. Please contact support."));
                }

        }

        private String prepareEmailBody(String emailType, String token) throws Exception {

                String fileContent = null;
                String result = null;
                Path fileName = null;

                switch (emailType) {
                        case "CONFIRMATION_TOKEN":
                                fileName = Path.of("src/main/resources/templates/verify-email.html");
                                fileContent = Files.readString(fileName);
                                result = fileContent.replace("CONFIRMATION_TOKEN", token);
                                SUBJECT = "Email Verification";
                                break;
                        case "RESET_PASSWORD_TOKEN":
                                fileName = Path.of("src/main/resources/templates/reset-password.html");
                                fileContent = Files.readString(fileName);
                                result = fileContent.replace("RESET_PASSWORD_TOKEN", token).replace("EMAIL_ADDRESS",
                                                "email@email.com");
                                SUBJECT = "Reset Password";

                                break;
                        default:
                                break;
                }

                return result;
        }
}