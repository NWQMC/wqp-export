package gov.usgs.water.control;


import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	private static final ResponseEntity<String> _404_ = new ResponseEntity<String>(HttpStatus.NOT_FOUND);

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
			
			String csv = "Hello World!";
			
			LOGGER.trace("exited: good");
			return csv;
		} catch (Exception e) {
			LOGGER.trace("exited: b");
			return null;
		}
	}

}