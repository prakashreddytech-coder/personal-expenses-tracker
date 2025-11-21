package com.expense.tracker.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMonthlyReport(String to, byte[] bankReport, byte[] cardReport) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Monthly Expense Report");
        helper.setText("Please find attached your monthly expense reports for Banks and Credit Cards.");

        helper.addAttachment("Bank_Report.pdf", new ByteArrayResource(bankReport));
        helper.addAttachment("Credit_Card_Report.pdf", new ByteArrayResource(cardReport));

        mailSender.send(message);
    }
}
