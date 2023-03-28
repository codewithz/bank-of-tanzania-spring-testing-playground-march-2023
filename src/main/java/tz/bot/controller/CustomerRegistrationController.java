package tz.bot.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tz.bot.model.Customer;
import tz.bot.model.CustomerRegistrationRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
public class CustomerRegistrationController {

    public void registerNewCustomer
            (@Valid @RequestBody CustomerRegistrationRequest customerRegistrationRequest){

    }

}
