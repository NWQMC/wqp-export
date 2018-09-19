package gov.usgs.water.app;

import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public static final String AliveService_ALIVE_NOTES     ="Informs that the service is running from the root path.";
	public static final String AliveService_APPROOT_NOTES   ="Responds in the same way as alive only from the application path.";
	
	public static final String InputService_FORM_NOTES      ="Returns an interactive HTML Form page.";
	
	public static final String VersionService_VERSION_NOTES ="Returns information about the application version and release.";
	public static final String VersionService_APPPATH_NOTES ="Responds in the same way as version only from the application path.";
	
	public static final String StatsService_EXPORT_NOTES     ="Returns CSV export of WEB_SERVICE_LOG table";
	public static final String StatsService_COUNT_NOTES      ="Returns WEB_SERVICE_LOG table row count";
}
