package com.pailsom.flare.batch.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.NonNull;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "flare.base")
@Data
public class ApplicationProperties {

    @Value("flare.base.department-resource")
    private FlareResource departmentResource;

    @Value("flare.base.employee-resource")
    private FlareResource employeeResource;

    @Data
    static class FlareResource {
        private String name;

        @NonNull
        private String type;

        public Resource getResource() throws Exception {
            switch (type){
                case "file": return new ClassPathResource(this.name);
                case "url": return new UrlResource(this.name);
            }
            throw new Exception("Unknown Type resource found");
        }
    }


}
