package dot.demo.email;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailerController {

	@Autowired
	EmailerService emailerService;

	@PostMapping(value = "/sendemail")
	public EmailDto emailHtmlTemplate(@RequestBody EmailDto email) throws IOException, MessagingException {

		emailerService.sendEmail(email);

		return email;
	}

	@PostMapping(value = "/sendtextemail")
	public EmailDto emailTextTemplate(@RequestBody EmailDto email) throws IOException, MessagingException {

		emailerService.sendTextTemplateEmail(email);

		return email;
	}

	@PostMapping(value = "/sendhtmlemail")
	public EmailDto emailInHtmlTemplate(@RequestBody EmailDto email) throws IOException, MessagingException {

		emailerService.sendHtmlEmail(email);

		return email;
	}

	@PostMapping(value = "/sendemails")
	public Iterable<EmailDto> emailHtmlTemplate(@RequestBody List<EmailDto> emails)
			throws IOException, MessagingException {

		emailerService.sendEmails(emails);

		return emails;
	}

}
