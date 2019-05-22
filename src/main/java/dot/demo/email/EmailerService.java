package dot.demo.email;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailerService {

	private static final Logger log = LoggerFactory.getLogger(EmailerService.class);

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	TemplateEngine templateEngine;

	@Autowired
	TemplateEngine textTemplateEngine;

	@Autowired
	TemplateEngine htmlTemplateEngine;

	@Autowired
	TemplateEngine fileTemplateEngine;

	/**
	 * Send emails using templates in Emailer/Templates/ directory
	 * 
	 * @param EmailDto
	 * @return EmailDto
	 * @throws MessagingException
	 * @throws IOException
	 */
	public EmailDto sendEmail(EmailDto emailDto) throws MessagingException, IOException {

		// Prepare the evaluation context
		Context ctx = prepareContext(emailDto);

		// Prepare message using a Spring helper
		MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		// Prepare message using a Spring helper
		MimeMessageHelper message = prepareMessage(mimeMessage, emailDto);

		// Create the HTML body using Thymeleaf
		String htmlContent = this.fileTemplateEngine.process(emailDto.getTemplateName(), ctx);
		message.setText(htmlContent, true /* isHtml */);
		emailDto.setEmailedMessage(htmlContent);

		log.info("Processing email request: " + emailDto.toString());

		message = prepareStaticResources(message, emailDto);

		// Send mail
		this.mailSender.send(mimeMessage);

		this.fileTemplateEngine.clearTemplateCache();

		return emailDto;

	}

	/**
	 * Send email using Text template
	 * 
	 * @param EmailDto
	 * @return EmailDto
	 * @throws IOException
	 * @throws MessagingException
	 */
	public EmailDto sendTextTemplateEmail(EmailDto emailDto) 
			throws IOException, MessagingException {

		// Prepare email context
		Context ctx = prepareContext(emailDto);

		// Prepare message
		MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		// Prepare message using a Spring helper
		MimeMessageHelper message = prepareMessage(mimeMessage, emailDto);
		// Create email message using TEXT template
		String textContent = this.textTemplateEngine.process(emailDto.getTemplateName(), ctx); // text/email-text\"

		emailDto.setEmailedMessage(textContent);
		message.setText(textContent);

		// Send email
		this.mailSender.send(mimeMessage);

		return emailDto;

	}

	/**
	 * Send email with html template found in classpath resource
	 * 
	 * @param EmailDto
	 * @return EmailDto
	 * @throws MessagingException
	 * @throws IOException 
	 */
	public EmailDto sendHtmlEmail(EmailDto emailDto) throws MessagingException, IOException {
		// Prepare the evaluation context
		Context ctx = prepareContext(emailDto);
		
		// Prepare message using a Spring helper
		MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		MimeMessageHelper message = prepareMessage(mimeMessage, emailDto);

		// Create the HTML body using Thymeleaf
		String htmlContent = this.htmlTemplateEngine.process(emailDto.getTemplateName(), ctx);
		message.setText(htmlContent, true /* isHtml */);
		emailDto.setEmailedMessage(htmlContent);

		log.info("Processing html email request: " + emailDto.toString());

	    message = prepareStaticResources(message, emailDto);

		// Send mail
		this.mailSender.send(mimeMessage);

		this.htmlTemplateEngine.clearTemplateCache();

		return emailDto;

	}

	
	/**
	 * Send multiple emails using templates in Emailer/Templates/ directory
	 * 
	 * @param emailDtos
	 * @return
	 * @throws MessagingException
	 * @throws IOException 
	 */
	public List<EmailDto> sendEmails(List<EmailDto> emailDtos) throws MessagingException, IOException {

		List<MimeMessage> mimeMessages = new ArrayList<MimeMessage>();
		MimeMessage mimeMessage = null;

		for (EmailDto emailDto : emailDtos) {

			// Prepare the evaluation context
			final Context ctx = prepareContext(emailDto);

			// Prepare message using a Spring helper
			mimeMessage = this.mailSender.createMimeMessage();
			MimeMessageHelper message = prepareMessage(mimeMessage, emailDto);

			// Create the HTML body using Thymeleaf
			String htmlContent = this.fileTemplateEngine.process(emailDto.getTemplateName(), ctx);
			message.setText(htmlContent, true /* isHtml */);
			emailDto.setEmailedMessage(htmlContent);

			log.info("Processing emails request: " + emailDto.toString());

			message = prepareStaticResources(message, emailDto);

			mimeMessages.add(mimeMessage);
		}

		// Send mail
		this.mailSender.send(mimeMessages.toArray(new MimeMessage[0]));

		this.fileTemplateEngine.clearTemplateCache();

		return emailDtos;

	}

	private MimeMessageHelper prepareMessage(MimeMessage mimeMessage, EmailDto emailDto)
			throws MessagingException, IOException {

		// Prepare message using a Spring helper
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				"UTF-8");
		message.setSubject(emailDto.getSubject());
		message.setFrom(emailDto.getFrom());
		message.setTo(emailDto.getTo());

		if (emailDto.getCc() != null && emailDto.getCc().length != 0) {
			message.setCc(emailDto.getCc());
		}

		if (emailDto.getBcc() != null && emailDto.getBcc().length != 0) {
			message.setBcc(emailDto.getBcc());
		}

		if (emailDto.isHasAttachment()) {
			List<File> attachments = loadResources(
					emailDto.getPathToAttachment() + "/*" + emailDto.getAttachmentName() + "*.*");
			for (File file : attachments) {
				message.addAttachment(file.getName(), file);
			}
		}

		return message;

	}

	private List<File> loadResources(String fileNamePattern) throws IOException {
		PathMatchingResourcePatternResolver fileResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = null;
		
		try {
			resources = fileResolver.getResources("file:" + fileNamePattern);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<File> attachFiles = new ArrayList<File>();

		for (Resource resource : resources) {
			attachFiles.add(resource.getFile());
		}

		return attachFiles;

	}

	private Context prepareContext(EmailDto emailDto) {
		// Prepare the evaluation context
		Context ctx = new Context();
		Set<String> keySet = emailDto.getParameterMap().keySet();
		keySet.forEach(s -> {
			ctx.setVariable(s, emailDto.getParameterMap().get(s));
		});

		Set<String> resKeySet = emailDto.getStaticResourceMap().keySet();
		resKeySet.forEach(s -> {
			ctx.setVariable(s, emailDto.getStaticResourceMap().get(s));
		});

		return ctx;
	}

	private MimeMessageHelper prepareStaticResources(MimeMessageHelper message, 
			EmailDto emailDto) throws MessagingException {
		Map<String, Object> staticResources = emailDto.getStaticResourceMap();

		for (Map.Entry<String, Object> entry : staticResources.entrySet()) {
			
			ClassPathResource imageSource = 
					new ClassPathResource("static/" + (String) entry.getValue());
			message.addInline(entry.getKey(), imageSource, "image/png");
			message.addInline((String) entry.getValue(), imageSource, "image/png");

		}

		return message;
	}

}
