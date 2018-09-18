package gov.usgs.water.control;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.water.app.SwaggerConfig;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/wqp-export")
@CrossOrigin(origins = "*") // no credentials by default
//@Api("ExportService")
public class Service {
	private static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Service.class);


	@Autowired
	JdbcTemplate jdbcTemplate;


	@ApiOperation(
			value = "Export Service",
			notes = SwaggerConfig.StatsService_EXPORT_NOTES
		)
	@GetMapping(value = "/export",
			produces = "text/csv"
		)
	public String export() {

		try {
			LOGGER.trace("entered");

			String csv = executeExport();

			LOGGER.trace("exited: good");
			return csv;
		} catch (Exception e) {
			LOGGER.trace("exited: b");
		}
		return "";
	}


	@ApiOperation(
			value = "Count Service",
			notes = SwaggerConfig.StatsService_EXPORT_NOTES
		)
	@GetMapping(value = "/count",
			produces = "text/csv"
		)
	public String count() {

		try {
			LOGGER.trace("entered");
			
			String count = executeCount();
			
			LOGGER.trace("exited: good");
			return count;
		} catch (Exception e) {
			LOGGER.trace("exited: b");
		}
		return "";
	}


	public String executeCount() throws Exception {

		LOGGER.info("exporting data");

		String result = jdbcTemplate.query(
			"select count(*) as total from web_service_log",
			rs -> {
				StringBuilder builder = new StringBuilder();
				while (rs.next()) {
					builder.append( rs.getString("total") );
				}
				return builder.toString();
			}
		);

		LOGGER.debug(result);
		return result;
	}


	public String executeExport() throws Exception {

		LOGGER.info("exporting data");

		String result = jdbcTemplate.query(
			"select * from web_service_log",
			rs -> {
				StringBuilder builder = new StringBuilder();
				while (rs.next()) {
					builder.append( rs.getString("") );
				}
				return builder.toString();
			}
		);

		LOGGER.debug(result);
		return result;
	}

}