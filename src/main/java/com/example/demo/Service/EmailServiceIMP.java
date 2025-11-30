package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.Model.CommonModel.EmailDetail;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceIMP implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailDetail emailDetail) {
        try {
            // Tạo đối tượng MimeMessage
            MimeMessage message = mailSender.createMimeMessage();

            // Dùng helper để set thông tin
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("your_email@example.com"); 
            helper.setTo(emailDetail.getRecipient());                   
            helper.setSubject(emailDetail.getSubject());              
            helper.setText(emailDetail.getMsgBody(), true);              

            // Gửi mail
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        
    }   
}

