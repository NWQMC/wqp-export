package gov.usgs.water.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.jdbc.core.JdbcTemplate;

import oracle.jdbc.driver.OracleDriver;

@SpringBootApplication(scanBasePackages = "gov.usgs.water")
public class Application extends SpringBootServletInitializer implements CommandLineRunner {

	 @Autowired
	    JdbcTemplate jdbcTemplate;
	 
	 public static void main(String[] args) {
    	OracleDriver driver = null;
    	System.err.println(driver);
        SpringApplication.run(Application.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		executeExport();
	}
	
    public void executeExport() throws Exception {

        System.err.println("exporting data");

        
        String result = jdbcTemplate.query(
			"select count(*) as total from web_service_log", //new Object[] {},
	        rs -> {
	        	StringBuilder builder = new StringBuilder();
	        	while (rs.next()) {
	        		builder.append( rs.getString("total") );
	        	}
	        	return builder.toString();
	        }
        );
        System.err.println(result);
    }
}
