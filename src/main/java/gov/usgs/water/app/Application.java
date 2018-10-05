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

	public static final String DEFAULT_EXPORT_FILE = "defaultFile";
	public static final String SERVICE_INDICATION  = "service";
	
	// export instance for command line runs only
	@Autowired
	private Export export;

	// spring-boot bootstrap
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	// spring-boot command line runner interface
	@Override
	public void run(String... args) {
		
		// default filename from application.properties
		String filename = AppConfig.getExportFileName();
		
		// if there are args then store the first as the export filename
		if (args.length > 0) {
			filename = args[0];
		}
		
		if ( isCommandLineMode(filename) ) {
			exportRecords(filename);
			doNotRunService();
		}
	}

	// There are two first argument/parameter indications of running 
	// as a service vs command line. One is the SERVICE_INDEICATION and
	// another is within the Spring STS eclipse IDE launcher first arg w/ '--'.
	// If specifying service or spring STS ide service launcher arg 
	// then do not exec command line options. Otherwise, run cmd and exit.
	private boolean isCommandLineMode(String filename) {
		boolean isCmdLineMode = ! SERVICE_INDICATION.equals(filename) && ! filename.startsWith("--");
		return isCmdLineMode;
	}

	// export the records to the file
	private void exportRecords(String filename) {
		try {
			filename = decorateFilenameWithDate(filename);
			logRecordCount(filename);
			export.execute(filename);
		} catch (Exception e) {
			// it is ok to use println for command line tools
			System.err.println("Error exporting records.");
			e.printStackTrace();
		}
	}

	// change given base name to filename-year-month.csv
	private String decorateFilenameWithDate(String filename) {
		// if special default export file name trigger is supplied then use default.
		if (DEFAULT_EXPORT_FILE.equals(filename)) {
			filename = AppConfig.getExportFileName();
		}
		// decorate the file with year-month.csv
		filename = AppConfig.decorateExportFileName(filename);
		return filename;
	}

	// "log" to the command line the count of exported records
	private void logRecordCount(String filename) throws Exception {
		String count = export.fetchCount();
		// it is ok to use println for command line tools
		System.out.println();
		System.out.println();
		System.out.println("writing "+ count + " records to "+ new File(filename).getAbsolutePath());
		System.out.println();
		System.out.println();
	}
	
	// do not run the service in command line mode
	private void doNotRunService() {
		System.exit(0);
	}

}
