package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	@Bean
	public ServletWebServerFactory servletContainer() {
		JettyServletWebServerFactory jettyServletWebServerFactory = new JettyServletWebServerFactory();
		return jettyServletWebServerFactory;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
