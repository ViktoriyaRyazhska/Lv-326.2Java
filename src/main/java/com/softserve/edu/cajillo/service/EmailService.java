package com.softserve.edu.cajillo.service;

public interface EmailService {
    void sendEmail(String email, String topic, String text);
}
