package gov.usgs.water.control;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.water.app.AppConfig;
import gov.usgs.water.app.SwaggerConfig;
import gov.usgs.water.logic.Export;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/wqp-export")
@CrossOrigin(origins = "*") // no credentials by default
//@Api("ExportService")
public class Service {
	private static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Service.class);

	@Autowired
	private Export export;
	public void setExport(Export export) {
		this.export = export;
	}


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

			String csv = export.execute();

			LOGGER.trace("exited: good");
			return csv;
		} catch (Exception e) {
			LOGGER.error("exited: bad", e);
		}
		return "";
	}


	@ApiOperation(
			value = "Export to file Service",
			notes = SwaggerConfig.StatsService_EXPORT_NOTES
		)
	@GetMapping(value = "/fileExport",
			produces = "text/csv"
		)
	public String fileExport() {

		try {
			LOGGER.trace("entered");

			String csv = export.execute(AppConfig.getExportFileName());

			LOGGER.trace("exited: good");
			return csv;
		} catch (Exception e) {
			LOGGER.error("exited: bad", e);
		}
		return "";
	}
	
	
	@ApiOperation(
			value = "Count Service",
			notes = SwaggerConfig.StatsService_COUNT_NOTES
		)
	@GetMapping(value = "/count",
			produces = "text/csv"
		)
	public String count() {

		try {
			LOGGER.trace("entered");
			
			String count = export.fetchCount();
			
			LOGGER.trace("exited: good");
			return count;
		} catch (Exception e) {
			LOGGER.error("exited: bad", e);
		}
		return "";
	}

}