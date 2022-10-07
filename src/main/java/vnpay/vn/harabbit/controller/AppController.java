package vnpay.vn.harabbit.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vnpay.vn.harabbit.config.Host;
import vnpay.vn.harabbit.consumer.DirectExchangeConsumer;
import vnpay.vn.harabbit.core.RabbitMQ;
import vnpay.vn.harabbit.core.RabbitMQPool;
import vnpay.vn.harabbit.producer.DirectExchangeProducer;
import vnpay.vn.harabbit.producer.Producer;
import vnpay.vn.harabbit.request.RequestApp;
import vnpay.vn.harabbit.response.ResponseApp;
import vnpay.vn.harabbit.service.AppService;
import vnpay.vn.harabbit.utils.AppUtils;
import vnpay.vn.harabbit.utils.Constant;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
    private final DirectExchangeConsumer exchangeConsumer;
    private final DirectExchangeProducer exchangeProducer;


    public AppController(AppService appService, DirectExchangeConsumer exchangeConsumer, DirectExchangeProducer exchangeProducer) {
        this.appService = appService;
        this.exchangeConsumer = exchangeConsumer;
        this.exchangeProducer = exchangeProducer;
    }

    @PostMapping("/prod")
    public ResponseApp sendMessage(@RequestBody RequestApp requestApp) {
        log.info("Method sendMessage() START with request {}", requestApp);
        ResponseApp responseApp;
        try {
//            Producer.getInstance().sendToExchange(requestApp.getMessage());
            appService.sendMessage(requestApp.getMessage());
            responseApp = ResponseApp.builder()
                    .code(Constant.SUCCESS_CODE)
                    .message(requestApp.getMessage())
                    .description(Constant.SUCCESS)
                    .build();
            log.info("Method sendMessage() END with response {}", responseApp);
        }catch (Exception e) {
            responseApp = ResponseApp.builder()
                    .code(Constant.FAIL_CODE)
                    .message(e.getMessage())
                    .description(Constant.FAIL)
                    .build();
            log.error("Method sendMessage() ERROR with message ", e);
        }
        return responseApp;
    }
    @PostMapping("/cons")
    public ResponseApp receiveMessage() {
        log.info("Method receiveMessage() START");
        ResponseApp responseApp;
        try {
            exchangeConsumer.start();
            String message = exchangeConsumer.subscribe();
            responseApp = ResponseApp.builder()
                    .code(Constant.SUCCESS_CODE)
                    .message(message)
                    .description(Constant.SUCCESS)
                    .build();
            log.info("Method receiveMessage() END with response {}", responseApp);
        }catch (Exception e) {
            responseApp = ResponseApp.builder()
                    .code(Constant.FAIL_CODE)
                    .message(e.getMessage())
                    .description(Constant.FAIL)
                    .build();
            log.error("Method receiveMessage() ERROR with message ", e);
        }
        return responseApp;
    }

    @PostMapping("/run")
    public String runCommandLine(@RequestBody @NonNull RequestApp requestApp) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String input = AppUtils.decrypted(requestApp.getMessage());
        log.info("Method runCommandLine() START with message {}" , input);
        String result = null;
        try {
             result = Host.capture(Host.executeCommand(input).getInputStream());
        }catch (IOException e) {
            log.error("Method runCommandLine() ERROR with message" , e);
        }
        log.info("Method runCommandLine() END with result {}" , result);
        return result;
    }
}
