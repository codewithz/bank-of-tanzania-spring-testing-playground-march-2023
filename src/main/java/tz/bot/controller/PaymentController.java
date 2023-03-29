package tz.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping ("/payment")
    @ResponseStatus(HttpStatus.CREATED)
    public void makePayment(@RequestBody PaymentRequest paymentRequest){
        paymentService.chargeCard(paymentRequest.getPayment().getCustomerId(),paymentRequest);
    }
}
