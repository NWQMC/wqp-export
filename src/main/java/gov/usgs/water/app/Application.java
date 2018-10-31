package gov.usgs.water.app;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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
	
	public static final Pattern YYYY_MM = Pattern.compile("^\\d\\d\\d\\d[-/]\\d\\d$");
	
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
			int mm = 7; // start Jul 2016
			for (int yyyy=2016; yyyy<=2018; yyyy++) {
				while(mm <= 12) {
					String yyyy_mm = String.format("%4d-%02d", yyyy, mm);
					if ("2018_10".equals(yyyy_mm) ) {
						break;
					}
					System.out.println(yyyy_mm);
					exportRecords(filename, yyyy_mm);
					mm++;
				}
				mm = 1;
			}
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
	private void exportRecords(String filename, String yyyy_mm) {
		try {
			if (StringUtils.isBlank(yyyy_mm)) {
				Integer[] yearMonth = Export.currentYearMonth();
				yyyy_mm = yearMonth[0] +"-"+ yearMonth[1];
			}			
			filename = decorateFilenameWithDate(filename, yyyy_mm);
			String count = export.fetchCount(yyyy_mm);
			// it is ok to use println for command line tools
			System.out.println();
			System.out.println();
			System.out.println("writing "+ count +" records for "+ yyyy_mm +" to "+ new File(filename).getAbsolutePath());
			System.out.println();
			System.out.println();
			export.execute(filename, yyyy_mm);
		} catch (Exception e) {
			// it is ok to use println for command line tools
			System.err.println("Error exporting records.");
			e.printStackTrace();
		}
	}

	// change given base name to filename-year-month.csv
	private String decorateFilenameWithDate(String filename, String yyyy_mm) {
		// if special default export file name trigger is supplied then use default.
		if (DEFAULT_EXPORT_FILE.equals(filename)) {
			filename = AppConfig.getExportFileName();
		}
		// decorate the file with year-month.csv
		filename = AppConfig.decorateExportFileName(filename, yyyy_mm);
		return filename;
	}
	
	// do not run the service in command line mode
	private void doNotRunService() {
		System.exit(0);
	}

}
