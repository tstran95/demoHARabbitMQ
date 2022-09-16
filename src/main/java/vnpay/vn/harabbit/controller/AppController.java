package vnpay.vn.harabbit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vnpay.vn.harabbit.request.RequestApp;
import vnpay.vn.harabbit.response.ResponseApp;
import vnpay.vn.harabbit.service.AppService;

/**
 * @author sontt1
 * Date:9/14/2022
 * Time:4:59 PM
 */
@RestController
@RequestMapping("/api/v1/ha")
@Slf4j
public class AppController {
    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping
    public ResponseApp sendMessage(@RequestBody RequestApp requestApp) {
        log.info("Method sendMessage() START with request {}", requestApp);
        appService.sendMessage(requestApp.getMessage());
        ResponseApp responseApp = ResponseApp.builder()
                .code("01")
                .message(requestApp.getMessage())
                .description("OK")
                .build();
        log.info("Method sendMessage() END with response {}", responseApp);
        return responseApp;
    }
}
