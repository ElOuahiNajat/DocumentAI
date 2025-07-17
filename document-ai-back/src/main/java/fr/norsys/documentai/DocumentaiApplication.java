package fr.norsys.documentai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class DocumentaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentaiApplication.class, args);
	}

}
