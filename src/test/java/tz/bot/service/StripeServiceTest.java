package tz.bot.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tz.bot.model.CardPaymentCharge;
import tz.bot.model.Currency;

import java.math.BigDecimal;
import java.util.Map;


import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;

class StripeServiceTest {

    private StripeService underTest;

    @Mock
    private StripeApi stripeApi;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        underTest=new StripeService(stripeApi);
    }

    @Test
    void itShouldChargeCard() throws StripeException {
        //Given
        String cardSource="0l0l0l0l";
        BigDecimal amount=new BigDecimal("10.00");
        Currency currency=Currency.GBP;
        String description="Online Shopping";

        //... Successful Charge
        Charge charge=new Charge();
        charge.setPaid(true);
        given(stripeApi.create(anyMap(),any()))
                .willReturn(charge);
        //When
        CardPaymentCharge cardPaymentCharge=underTest.
                chargeCard(cardSource,
                        amount,
                        currency,
                        description);

        //Then
        ArgumentCaptor<Map<String,Object>> mapArgumentCaptor=ArgumentCaptor.
                                                                forClass(Map.class);
        ArgumentCaptor<RequestOptions> requestOptionsArgumentCaptor=ArgumentCaptor.
                                                                forClass(RequestOptions.class);

        //Captor RequestMap and Options
        then(stripeApi).should().create(mapArgumentCaptor.capture(),requestOptionsArgumentCaptor.capture());

        //Asserts on request map
        Map<String,Object> requestMap=mapArgumentCaptor.getValue();
        assertThat(requestMap.keySet()).hasSize(4);

        assertThat(requestMap.get("amount")).isEqualTo(amount);
        assertThat(requestMap.get("currency")).isEqualTo(currency);
       // assertThat(requestMap.get("cardSource")).isEqualTo(cardSource);
        assertThat(requestMap.get("description")).isEqualTo(description);

        //.. Asserts on Request Options
        RequestOptions options=requestOptionsArgumentCaptor.getValue();

        assertThat(options).isNotNull();

        //... Card is debited successfully

        assertThat(cardPaymentCharge).isNotNull();
        assertThat(cardPaymentCharge.isCardDebited()).isTrue();
    }
}