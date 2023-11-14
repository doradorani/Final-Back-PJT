package com.office.agijagi_back.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/")
public class HealthCheckController {
    @GetMapping("/healthCheck")
    public String healthCheck(){
        log.info("[HealthCheckController] healthCheck");
        return "healthcheck";
    }
}
