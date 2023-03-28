package tz.bot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tz.bot.model.Currency;
import tz.bot.model.Payment;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import static  org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository underTest;

    @Test
    void itShouldInsertPayment() {
        //Given
        Long paymentId=1L;
        Payment payment=new Payment(
                null,
                UUID.randomUUID(),
                new BigDecimal("10.00"),
                Currency.EUR,
                "card1234",
                "Donation"
        );
        //When
        underTest.save(payment);
        //Then
        Optional<Payment> paymentOptional=underTest.findById(paymentId);
        assertThat(paymentOptional)
                .isPresent()
                .hasValueSatisfying(p->assertThat(p).isEqualTo(payment));
    }
}