package tz.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tz.bot.model.PaymentRequest;
import tz.bot.service.PaymentService;

@RestController
@RequestMapping("api/v1")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @RequestMapping("/payment")
    public void makePayment(@RequestBody PaymentRequest paymentRequest){
        paymentService.chargeCard(paymentRequest.getPayment().getCustomerId(),paymentRequest);
    }
}
