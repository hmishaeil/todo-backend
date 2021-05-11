package com.example.todo.services;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class AwsSesService {
        private static final String CHAR_SET = "UTF-8";
        private static final String SUBJECT = "Test from our application";
        private final AmazonSimpleEmailService emailService;
        private final String sender;

        @Autowired
        public AwsSesService(AmazonSimpleEmailService emailService, @Value("${email.sender}") String sender) {
                this.emailService = emailService;
                this.sender = sender;
        }

        public void sendEmail(String email, String confirmationToken) {
                try {

                        Path fileName = Path.of("src/main/resources/templates/verify_email/verify_email.html");
                        String actual = Files.readString(fileName);

                        String replaced = actual.replace("CONFIRMATION_TOKEN", confirmationToken);

                        int requestTimeout = 3000;
                        SendEmailRequest request = new SendEmailRequest()
                                        .withDestination(new Destination().withToAddresses(email))
                                        .withMessage(new Message()
                                                        .withBody(new Body().withHtml(new Content()
                                                                        .withCharset(CHAR_SET).withData(replaced)))
                                                        .withSubject(new Content().withCharset(CHAR_SET)
                                                                        .withData(SUBJECT)))
                                        .withSource(sender).withSdkRequestTimeout(requestTimeout);
                        emailService.sendEmail(request);
                } catch (Exception e) {
                        log.error("Error occurred sending email to {} ", email, e);
                }
        }

        public void sendResetEmail(String email, String resetPasswordToken) {
                try {

                        Path fileName = Path.of("src/main/resources/templates/reset_password/reset_password.html");
                        String actual = Files.readString(fileName);

                        String replaced = actual.replace("RESET_PASSWORD_TOKEN", resetPasswordToken)
                                        .replace("EMAIL_ADDRESS", email);

                        int requestTimeout = 3000;
                        SendEmailRequest request = new SendEmailRequest()
                                        .withDestination(new Destination().withToAddresses(email))
                                        .withMessage(new Message()
                                                        .withBody(new Body().withHtml(new Content()
                                                                        .withCharset(CHAR_SET).withData(replaced)))
                                                        .withSubject(new Content().withCharset(CHAR_SET)
                                                                        .withData(SUBJECT)))
                                        .withSource(sender).withSdkRequestTimeout(requestTimeout);
                        emailService.sendEmail(request);
                } catch (Exception e) {
                        log.error("Error occurred sending email to {} ", email, e);
                }
        }
}