package sw.goku.ticket.bussiness.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.thymeleaf.context.Context;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendTemplateEmail(String to, String subject, Map<String, Object> templateModel, String template) throws MessagingException, MessagingException {
        // Crear el contexto de Thymeleaf
        Context context = new Context();
        context.setVariables(templateModel);

        // Procesar el template
        String htmlBody = templateEngine.process(template, context);

        // Crear el mensaje MIME
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        // Enviar el mensaje
        mailSender.send(message);
    }
}
