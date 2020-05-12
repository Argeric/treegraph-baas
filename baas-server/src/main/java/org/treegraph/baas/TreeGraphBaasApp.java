package org.treegraph.baas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@PropertySource(value= {"classpath:config-biz.properties","classpath:config-yw.properties"})
//@ImportResource(locations = "classpath:spring/dubbo-*.xml")
@EnableConfigurationProperties
@EnableScheduling
public class  TreeGraphBaasApp extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(TreeGraphBaasApp.class, args);
    }
    
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TreeGraphBaasApp.class);
    }
}