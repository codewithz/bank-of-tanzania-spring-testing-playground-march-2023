package tz.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tz.bot.model.CardPaymentCharger;
import tz.bot.model.Currency;
import tz.bot.repository.CustomerRepository;
import tz.bot.repository.PaymentRepository;

import java.util.List;

@Service
public class PaymentService {

    private static final List<Currency> ACCEPTED_CURRENCIES=List.of(Currency.GBP,Currency.USD,Currency.EUR);

    private CustomerRepository customerRepository;
    private PaymentRepository paymentRepository;
    private CardPaymentCharger cardPaymentCharger;



}
