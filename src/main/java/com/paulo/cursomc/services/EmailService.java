package com.paulo.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.paulo.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfimationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
