package gov.usgs.water.app;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import gov.usgs.water.logic.Export;

@SpringBootApplication(scanBasePackages = "gov.usgs.water")
public class Application extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private Export export;

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		String filename = AppConfig.getExportFileName();
		if (args.length > 0) {
			filename = AppConfig.decorateExportFileName(args[0]);
		}
		
		// if specifying service we do not want command line
		// and spring sts service launcher sets a -- param
		if ( ! "service".equals(filename) && ! filename.startsWith("--") ) {
			if ("defaultFile".equals(filename)) {
				filename = AppConfig.getExportFileName();
			}
			String count = export.fetchCount();
	
			System.out.println();
			System.out.println();
			System.out.println("writing "+ count + " records to "+ new File(filename).getAbsolutePath());
			System.out.println();
			System.out.println();
	
			export.execute(filename);
	
			System.exit(0);
		}
	}
	
}
