package com.team1.spreet.global.common.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Swagger")
@RestController
@RequestMapping("/api")
public class SwaggerController {
	@ApiOperation(value = "Swagger Document API")
	@GetMapping("/doc")
	public void redirectSwagger(HttpServletResponse response) throws IOException {
		response.sendRedirect("https://sparta-rara.shop/swagger-ui/index.html");
//		response.sendRedirect("http://52.78.92.15/swagger-ui/index.html");
//		response.sendRedirect("http://localhost:8080/swagger-ui/index.html");
	}
}
