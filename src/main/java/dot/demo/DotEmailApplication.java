package dot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
@Configuration
@SpringBootApplication
public class DotEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(DotEmailApplication.class, args);
	}
	
}
