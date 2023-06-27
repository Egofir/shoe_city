package com.by.service;

import com.by.entity.Email;

import javax.mail.MessagingException;

public interface IEmailService {

    void sendEmail(Email email) throws MessagingException;

}
