package ${package}.mail;

import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring3.SpringTemplateEngine;


public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	public void sendMailWithInline(final String recipientEmail, 
			  final String from, final String subject, final String templateName,
			  final Map<String, Object> vars, final Locale locale)
			  throws MessagingException {
			 
	  // Prepare the evaluation context
	  final Context ctx = new Context(locale);
	  ctx.setVariables(vars);
	  
	  // Prepare message using a Spring helper
	  final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
	  final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
	  message.setSubject(subject);
	  message.setFrom(from);
	  message.setTo(recipientEmail);
	 
	  // Create the HTML body using Thymeleaf
	  final String htmlContent = this.templateEngine.process(templateName, ctx);
	  message.setText(htmlContent, true); // true = isHtml
	 
	  // Send mail
	  this.mailSender.send(mimeMessage);
	 
	}
	
}
