package vnpay.vn.harabbit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import vnpay.vn.harabbit.request.RequestApp;
import vnpay.vn.harabbit.response.ResponseApp;
import vnpay.vn.harabbit.service.AppService;

/**
 * @author sontt1
 * Date:9/14/2022
 * Time:4:59 PM
 */
@Controller
@RequestMapping("/api/v1/ha")
public class AppController {
    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping
    public ResponseApp sendMessage(@RequestBody RequestApp requestApp) {
        appService.sendMessage(requestApp.getMessage());
        System.out.println(requestApp.getMessage());
        return ResponseApp.builder()
                .code("01")
                .message(requestApp.getMessage())
                .description("OK")
                .build();
    }
}
