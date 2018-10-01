package gov.usgs.water.app;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import gov.usgs.water.logic.Export;


@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
public class AppConfig {
	public static String MESSAGE_406;
	
	private static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AppConfig.class);

	private static String exportFileName;
	public static String getExportFileName() {
		return exportFileName;
	}
	public void setExportFileName() {
		String exportFileName = env.getProperty("export.filename", "wqp-export");
		int dot = exportFileName.lastIndexOf('.');
		if (dot >= exportFileName.length()-4) {
			exportFileName = exportFileName.substring(0, dot);
		}
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		int year  = today.getYear();
		exportFileName = exportFileName+String.format("-%d-%d.csv", year, month);
	}

	@Autowired
	private Environment env;
	 
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
 
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean
	public Export export() {
		return new Export();
	}
 
	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
 
	@Bean
	public DataSource dataSource() {
		setExportFileName();
		LOGGER.info("Export File Name: {}", exportFileName);

		
		String jdbcServer = env.getProperty("jdbc.server", "not specified");
		LOGGER.info("Setting up data source: {}", jdbcServer);

		SingleConnectionDataSource ds = new SingleConnectionDataSource();
		ds.setDriverClassName(oracle.jdbc.driver.OracleDriver.class.getName());
		ds.setUrl("jdbc:oracle:thin:@" + jdbcServer);
		ds.setUsername(env.getProperty("jdbc.username"));
		ds.setPassword(env.getProperty("jdbc.password"));
		
		return ds;
	}
	
}