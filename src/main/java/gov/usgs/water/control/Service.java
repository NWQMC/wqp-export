package gov.usgs.water.control;


import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVWriter;

import gov.usgs.water.app.AppConfig;
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
			LOGGER.trace("exited: bad");
		}
		return "";
	}


	@ApiOperation(
			value = "Export Service",
			notes = SwaggerConfig.StatsService_EXPORT_NOTES
		)
	@GetMapping(value = "/fileExport",
			produces = "text/csv"
		)
	public String fileExport() {

		try {
			LOGGER.trace("entered");

			String csv = executeExport(AppConfig.getExportFileName());

			LOGGER.trace("exited: good");
			return csv;
		} catch (Exception e) {
			LOGGER.trace("exited: bad");
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
			LOGGER.trace("exited: bad");
		}
		return "";
	}


	public String executeCount() throws Exception {

		LOGGER.info("getting row count");

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
		return executeExport(null);
	}

	public String executeExport(String toFile) throws Exception {
		// outside the try resource management is okay because OpenCSV closes parents
		Writer writer;
		if (StringUtils.isNotBlank(toFile)) {
			File file = new File(toFile);
			LOGGER.info("exporting data to file: {}", file.getAbsolutePath());
			writer = new FileWriter(file);
		} else {
			LOGGER.info("exporting data");
			writer = new StringWriter();
		}

		String result = jdbcTemplate.query(
				// TODO remove 5 restriction
			"select * from web_service_log where rownum < 5",
			rs -> {
				int cols = rs.getMetaData().getColumnCount();
				String[] line = new String[cols];

				try (CSVWriter csv = new CSVWriter(writer)) {
					while (rs.next()) {
						for (int col=0; col<cols;) {
							line[col] = rs.getString(++col);
						}
						csv.writeNext(line, false);
					}
					csv.flush();
					writer.flush();
					return "success";
				} catch (Exception e) {
					e.printStackTrace();
					return "error writing csv file  " + e.getMessage();
				}
			}
		);

		if ("success".equals(result) && writer instanceof StringWriter) {
			StringWriter string = (StringWriter) writer;
			result = string.getBuffer().toString();
		}
		LOGGER.trace("entries\n{}",result);
		return result;
	}

}