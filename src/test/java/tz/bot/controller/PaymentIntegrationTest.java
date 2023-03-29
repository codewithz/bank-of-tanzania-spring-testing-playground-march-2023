package tz.bot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tz.bot.model.*;
import tz.bot.repository.PaymentRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        //Given
        //... A Customer
        UUID customerId=UUID.randomUUID();
        Customer customer=new Customer(customerId,"Thomas","000000");

        //... A customer registration request
        CustomerRegistrationRequest customerRegistrationRequest=new CustomerRegistrationRequest(customer);

        //... Register the Customer

        ResultActions customerResultAction=mockMvc
                .perform(put("/api/v1/customer-registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(Objects.requireNonNull(objectToJson(customerRegistrationRequest))));

        // ... Payment

        long paymentId=1L;

        Payment payment=new Payment(
                paymentId,
                customerId,
                new BigDecimal("10.00"),
                Currency.GBP,
                "1010101",
                "Donation");

        //... A Payment Request

        PaymentRequest paymentRequest=new PaymentRequest(payment);

        //... When a payment is sent

        ResultActions paymentResultActions=mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(paymentRequest)))
        );

        //.. Then both the customer and payment should have 201 status code

        customerResultAction.andExpect(status().isCreated());
        paymentResultActions.andExpect(status().isCreated());

        //... Payment is stored in the database

        assertThat(paymentRepository.findById(paymentId))
                .isPresent()
                .hasValueSatisfying(p->assertThat(p).isEqualToComparingFieldByField(payment));


    }

    private String objectToJson(Object object) {
        try {
            return new
                    ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }
}
