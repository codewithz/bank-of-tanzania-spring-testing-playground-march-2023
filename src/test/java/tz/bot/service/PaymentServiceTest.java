package tz.bot.service;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tz.bot.model.*;
import tz.bot.repository.CustomerRepository;
import tz.bot.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CardPaymentCharger cardPaymentCharger;


    private PaymentService underTest;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        underTest=new PaymentService(customerRepository,paymentRepository,cardPaymentCharger);
    }
    @Nested
    @DisplayName("Tests for chargeCard() in PaymentService")
    class ChargeCard {
        @Test
        void itShouldChargeCardSuccessfully() {
            //Given
            UUID customerId = UUID.randomUUID();

            //... Customer Exists
            given(customerRepository.findById(customerId))
                    .willReturn(Optional.of(mock(Customer.class)));

            //... Payment Request
            PaymentRequest paymentRequest = new PaymentRequest(
                    new Payment(
                            null,
                            null,
                            new BigDecimal("100.00"),
                            Currency.USD,
                            "Card12345",
                            "Donation")
            );
            //.... Card is charged successfully
            given(cardPaymentCharger.chargeCard(
                    paymentRequest.getPayment().getSource(),
                    paymentRequest.getPayment().getAmount(),
                    paymentRequest.getPayment().getCurrency(),
                    paymentRequest.getPayment().getDescription()
            ))
                    .willReturn(new CardPaymentCharge(true));
            //When
            underTest.chargeCard(customerId, paymentRequest);
            //Then
            ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);

            then(paymentRepository).should().save(paymentArgumentCaptor.capture());

            Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();
            assertThat(paymentArgumentCaptorValue)
                    .isEqualToIgnoringGivenFields(
                            paymentRequest.getPayment(), "customerId");

            assertThat(paymentArgumentCaptorValue.getCustomerId()).isEqualTo(customerId);

        }

        @Test
        void itShouldThrowAnExceptionWhenCardIsNotCharged() {
            //Given
            UUID customerId = UUID.randomUUID();

            //... Customer Exists
            given(customerRepository.findById(customerId))
                    .willReturn(Optional.of(mock(Customer.class)));

            //... Payment Request
            PaymentRequest paymentRequest = new PaymentRequest(
                    new Payment(
                            null,
                            null,
                            new BigDecimal("100.00"),
                            Currency.USD,
                            "Card12345",
                            "Donation")
            );
            //.... Card is charged successfully
            given(cardPaymentCharger.chargeCard(
                    paymentRequest.getPayment().getSource(),
                    paymentRequest.getPayment().getAmount(),
                    paymentRequest.getPayment().getCurrency(),
                    paymentRequest.getPayment().getDescription()
            ))
                    .willReturn(new CardPaymentCharge(false));
            //When

            //Then
            assertThatThrownBy(() -> underTest.chargeCard(customerId, paymentRequest))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining(String.format("Card not debited for customer %s", customerId));

            // .. No interaction with payment repository
//        then(paymentRepository).should(never()).save(any());
            then(paymentRepository).shouldHaveNoInteractions();
        }

        @Test
        void itShouldNotChargeCardAndThrowAnExceptionWhenCurrencyIsNotSupported() {
            //Given
            UUID customerId = UUID.randomUUID();

            //... Customer Exists
            given(customerRepository.findById(customerId))
                    .willReturn(Optional.of(mock(Customer.class)));
            //... Euros (as it is not under accepted currencies)
            Currency currency = Currency.EUR;
            //... Payment Request
            PaymentRequest paymentRequest = new PaymentRequest(
                    new Payment(
                            null,
                            null,
                            new BigDecimal("100.00"),
                            currency,
                            "Card123MN",
                            "Donation"
                    )
            );
            //When
            assertThatThrownBy(() -> underTest.chargeCard(customerId, paymentRequest))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining(String.format("Currency [%s] not supported", currency));

            //Then
            //... No Interaction with CardPaymentCharger
            then(cardPaymentCharger).shouldHaveNoInteractions();
            //... No Interaction with Payment Repository
            then(paymentRepository).shouldHaveNoInteractions();
        }

        @Test
//    @Disabled
        void itShouldNotChargeCardAndThrowAnExceptionWhenCustomerNotFound() {
            //Given
            UUID customerId = UUID.randomUUID();

            //... Customer does not Exists
            given(customerRepository.findById(customerId))
                    .willReturn(Optional.empty());
            //When
            //Then
            assertThatThrownBy(() -> underTest.chargeCard(customerId, new PaymentRequest(new Payment())))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining(String.format("Customer with id [%s] is not found", customerId));

            //... No Interactions with CardPaymentCharger and PaymentRepository
            then(cardPaymentCharger).shouldHaveNoInteractions();
            then(paymentRepository).shouldHaveNoInteractions();


        }
    }
}