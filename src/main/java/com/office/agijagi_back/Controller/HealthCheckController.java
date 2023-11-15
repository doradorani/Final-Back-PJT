package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.SingleResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Log4j2
@RequestMapping("/")
public class HealthCheckController {

    private final ResponseService responseService;

    public HealthCheckController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @ApiOperation(httpMethod = "GET"
            , value = "ALB의 HealthCheck"
            , notes = "Health Checking mehtod for alb"
            , response = String.class
            , responseContainer = "SingleResult")
    @GetMapping("/healthCheck")
    public SingleResult<String> healthCheck(){
        log.info("[HealthCheckController] healthCheck");
        return responseService.getSingleResult("healthy");
    }
}
