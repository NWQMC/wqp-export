package gov.usgs.water.logic;


import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.opencsv.CSVWriter;

public class Export {
	private static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Export.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;


	public String fetchCount() throws Exception {

		LOGGER.info("getting row count");

		String result = jdbcTemplate.query(
			"select count(*) as total from web_service_log",
			rs -> {
				if (rs.next()) {
					return rs.getString("total");
				}
				return "none";
			}
		);

		LOGGER.debug(result);
		return result;
	}

	public String execute() throws Exception {
		return execute(null);
	}

	public String execute(String toFile) throws Exception {
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
			"select * from web_service_log",
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