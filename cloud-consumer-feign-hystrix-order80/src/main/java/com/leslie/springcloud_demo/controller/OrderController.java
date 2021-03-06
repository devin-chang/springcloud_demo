package com.leslie.springcloud_demo.controller;

import com.leslie.springcloud_demo.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
@RequestMapping("/consumer")
public class OrderController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payment/hystrix/ok/{id}")
    public String paymentInfo_Ok(@PathVariable("id") Integer id){
        String info_ok = paymentService.paymentInfo_Ok(id);

        return info_ok;
    }

    @GetMapping("/payment/hystrix/timeout/{id}")
//    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000")
//    })
    @HystrixCommand
    public String paymentInfo_Timeout(@PathVariable("id") Integer id){
        String info_timeout = paymentService.paymentInfo_Timeout(id);

        return info_timeout;
    }

    public String paymentInfo_TimeoutHandler(Integer id){
        return "我是80客户端，系统繁忙或出错，请稍后再试。";
    }

    //全局fallback
    public String payment_Global_FallbackMethod(){
        return "来自80客户端,Global异常处理信息,请稍后重试";
    }
}
