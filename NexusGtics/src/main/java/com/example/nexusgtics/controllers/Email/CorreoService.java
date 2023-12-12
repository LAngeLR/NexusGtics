package com.example.nexusgtics.controllers.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CorreoService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarCorreo(String correoDestino, String asunto, String contenido) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(correoDestino);
        mensaje.setSubject(asunto);
        mensaje.setText(contenido);

        javaMailSender.send(mensaje);
    }
}
