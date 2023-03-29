package tz.bot.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tz.bot.model.CardPaymentCharge;
import tz.bot.model.CardPaymentCharger;
import tz.bot.model.Currency;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService implements CardPaymentCharger {

    private final StripeApi stripeApi;

    private final static RequestOptions requestOptions =RequestOptions
            .builder()
            .setApiKey("sk_test_51MquS1SICls1H3gzXPQXgGKdf6xjvtkzsHNyNhSyMteua1i7cgKWYUuKjFa4CpajRglnBvU8WC9X6Xm6Yb2MTek300OiFLWT9M")
            .build();

    @Autowired
    public StripeService(StripeApi stripeApi) {
        this.stripeApi = stripeApi;
    }

    @Override
    public CardPaymentCharge chargeCard(
            String cardSource,
            BigDecimal amount,
            Currency currency,
            String description) {

        Map<String,Object> params=new HashMap<>();
        params.put("amount",amount);
        params.put("currency",currency);
        params.put("source",cardSource);
        params.put("description",description);

        try{
            Charge charge=stripeApi.create(params,requestOptions);
            return  new CardPaymentCharge(charge.getPaid());
        }
        catch(StripeException e){
            throw  new IllegalStateException("Cannot make stripe charge",e);
        }
    }
}
