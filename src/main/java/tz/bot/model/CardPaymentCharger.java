package tz.bot.model;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public interface CardPaymentCharger {

    CardPaymentCharge chargeCard(
            String cardSource,
            BigDecimal amount,
            Currency currency,
            String description
    );
}
