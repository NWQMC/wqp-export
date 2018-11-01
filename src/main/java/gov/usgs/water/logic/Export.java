package gov.usgs.water.logic;


import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.opencsv.CSVWriter;

public class Export {
	private static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Export.class);

	private static final String SQL_WHERE_CLAUSE 
			="  where extract(year from request_timestamp_utc)) = ? "
			+"    and extract(month from request_timestamp_utc)) = ? ";
	
	private static final String SQL_COUNT_ROWS 
			="select count(*) as total "
			+"from web_service_log"
			+SQL_WHERE_CLAUSE;
	
	private static final String SQL_MONTH_ROWS
			=" select * "
			+"   from web_service_log "
			+SQL_WHERE_CLAUSE;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;


	public String fetchCount() throws Exception {
		Integer[] yearMonth = currentYearMonth();
		return fetchCount(yearMonth[0], yearMonth[1]);
	}
	public String fetchCount(String yyyy_mm) throws Exception {
		Integer[] yearMonth = yearMonth(yyyy_mm);
		return fetchCount(yearMonth[0], yearMonth[1]);
	}
	
	public String fetchCount(int year, int month) throws Exception {

		LOGGER.info("getting row count");

		String result = jdbcTemplate.query(
			SQL_COUNT_ROWS,
			rs -> {
				if (rs.next()) {
					return rs.getString("total");
				}
				return "none";
			},
			buildDateParameters(year, month)
		);

		LOGGER.debug(result);
		return result;
	}

	
	public Object[] buildDateParameters(int year, int month) {
		return new Object[] {year,month};
    }
	public static Integer[] currentYearMonth() {
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		int year  = today.getYear();
		return new Integer[] {year,month};
    }
	public Integer[] yearMonth(String yyyy_mm) {
		int year  = Integer.parseInt( yyyy_mm.substring(0, 4) );
		int month = Integer.parseInt( yyyy_mm.substring(5) );
		return new Integer[] {year,month};
    }
	
	public String execute() throws Exception {
		return execute(null);
	}

	public String execute(String toFile) throws Exception {
		Integer[] yearMonth = currentYearMonth();
		return execute(toFile, yearMonth[0], yearMonth[1]);
	}
	public String execute(String toFile, String yyyy_mm) throws Exception {
		Integer[] yearMonth = currentYearMonth();
		return execute(toFile, yearMonth[0], yearMonth[1]);
	}
	public String execute(String toFile, int year, int month) throws Exception {
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
			SQL_MONTH_ROWS,
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
			},
			buildDateParameters(year, month)
		);

		if ("success".equals(result) && writer instanceof StringWriter) {
			StringWriter string = (StringWriter) writer;
			result = string.getBuffer().toString();
		}
		LOGGER.trace("entries\n{}",result);
		return result;
	}

}