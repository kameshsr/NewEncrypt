package controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "application-controller", description = "Application Controller")
public class ApplicationController {
	@GetMapping("/")
	public String viewHomePage() {

		return "index";
	}

}
