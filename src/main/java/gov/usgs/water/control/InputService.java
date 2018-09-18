package gov.usgs.water.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.ApiOperation;

@Controller
public class InputService {

	@ApiOperation(
			value = "Input Form Service",
			notes = "simple html form")
    @GetMapping("/wqp-export/input")
    public String form() {
        return "/index.html";
    }

}
