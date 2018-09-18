package gov.usgs.water.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "gov.usgs.water")
public class Application extends SpringBootServletInitializer implements CommandLineRunner {
	 
	 public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
	}
	
}
