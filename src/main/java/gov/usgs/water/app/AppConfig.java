package gov.usgs.water.app;

import javax.sql.DataSource;

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

@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
public class AppConfig {

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
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
 
    @Bean
    public DataSource dataSource() {
    	System.err.println("Setting up data source");
    	
    	SingleConnectionDataSource ds = new SingleConnectionDataSource();
    	ds.setDriverClassName(oracle.jdbc.driver.OracleDriver.class.getName());
        ds.setUrl(env.getProperty("jdbc.url"));
        ds.setUsername(env.getProperty("jdbc.username"));
        ds.setPassword(env.getProperty("jdbc.password"));
        return ds;
    }
}