package com.example.airtraffic;

import com.example.airtraffic.service.QueueManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@Slf4j
public class ControllerApplication implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private QueueManager queueManager;

    public static void main(String[] args) {
        SpringApplication.run(ControllerApplication.class, args);
    }

    // make sure queue manager starts only when all components initialized and application is ready to process requests
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        queueManager.start();
        log.info("Aircraft queue manager started");
    }
}
