package com.team1.spreet.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SwaggerController {
	@GetMapping("/doc")
	public void redirectSwagger(HttpServletResponse response) throws IOException {
//		response.sendRedirect("http://52.78.92.15/swagger-ui/index.html");
		response.sendRedirect("http://localhost:8080/swagger-ui/index.html");
	}
}