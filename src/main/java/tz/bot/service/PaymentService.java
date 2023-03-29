package tz.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tz.bot.model.CardPaymentCharge;
import tz.bot.model.CardPaymentCharger;
import tz.bot.model.Currency;
import tz.bot.model.PaymentRequest;
import tz.bot.repository.CustomerRepository;
import tz.bot.repository.PaymentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private static final List<Currency> ACCEPTED_CURRENCIES=List.of(Currency.GBP,Currency.USD);

    private CustomerRepository customerRepository;
    private PaymentRepository paymentRepository;
    private CardPaymentCharger cardPaymentCharger;
    @Autowired
    public PaymentService(CustomerRepository customerRepository,
                          PaymentRepository paymentRepository,
                          CardPaymentCharger cardPaymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    void chargeCard(UUID customerId, PaymentRequest paymentRequest){
//        1. Does customer exist, if not throw an exception
        boolean isCustomerFound=customerRepository.findById(customerId).isPresent();
        if(!isCustomerFound){
            throw new IllegalStateException(
                    String.format("Customer with id [%s] is not found",customerId));
        }
//        2. Do we support the currency, if not throw an exception
        boolean isCurrencySupported=ACCEPTED_CURRENCIES.contains(paymentRequest.getPayment().getCurrency());
        if(!isCurrencySupported){
            String message=String.format("Currency [%s] not supported",
                    paymentRequest.getPayment().getCurrency());
            throw new IllegalStateException(message);
        }
//        3. Charge the card
        CardPaymentCharge cardPaymentCharge=cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        );
//        4. If not debited , throw an exception
        if(!cardPaymentCharge.isCardDebited()){
            throw  new IllegalStateException(
                    String.format("Card not debited for customer %s",customerId));
        }
//        5. Insert the payment
        paymentRequest.getPayment().setCustomerId(customerId);

        paymentRepository.save(paymentRequest.getPayment());

    }
}
