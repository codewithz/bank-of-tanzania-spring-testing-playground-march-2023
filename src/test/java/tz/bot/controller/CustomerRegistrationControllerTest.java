package tz.bot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tz.bot.model.Customer;
import tz.bot.model.CustomerRegistrationRequest;
import tz.bot.service.CustomerRegistrationService;
import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static  org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(CustomerRegistrationController.class)
class CustomerRegistrationControllerTest {

    @MockBean
    private CustomerRegistrationService customerRegistrationService;

    @Autowired
    private MockMvc mockMvc;



    @Test
    void itShouldRegisterNewCustomer() throws Exception {
        //Given a Customer
        UUID id=UUID.randomUUID();
        Customer customer=new Customer(id,"James","000000");

        //.. a request
        CustomerRegistrationRequest request=new CustomerRegistrationRequest(customer);

        doNothing().when(customerRegistrationService).registerNewCustomer(request);

        //When
        ResultActions customerRegistrationResultAction=mockMvc
                .perform(
                        put("/api/v1/customer-registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(Objects.requireNonNull(objectToJson(request)))
                );
        //Then
        customerRegistrationResultAction.andExpectAll(status().isCreated());
    }

    private String objectToJson(Object object){
        try {
            return  new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e){
            fail("Failed to convert to JSON");
            return null;
        }
    }
}