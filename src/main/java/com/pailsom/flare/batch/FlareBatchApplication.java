package com.pailsom.flare.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class FlareBatchApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(FlareBatchApplication.class);
		springApplication.addListeners(new PropertiesLogger());
		springApplication.run(args);
	}


}
