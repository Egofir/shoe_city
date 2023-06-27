package com.by.service.impl;

import com.by.entity.Email;
import com.by.service.IEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements IEmailService {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromUser;

    @Override
    public void sendEmail(Email email) throws MessagingException {

        // 1、创建邮件发送类
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // 2、创建邮件模板
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(email.getTitle()); // 标题
        helper.setTo(email.getToUser()); // 收件人
        helper.setFrom(fromUser); // 发件人
        helper.setText(email.getContent(), true);
        if (StringUtils.isEmpty(email.getCcUser())) {
            helper.setCc(new String[]{fromUser});
        } else {
            helper.setCc(new String[]{fromUser, email.getCcUser()}); // 163发送邮件抄送给自己
        }

        // 3、发送
        mailSender.send(mimeMessage);

    }

}
