package tz.bot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tz.bot.model.Customer;
import tz.bot.model.CustomerRegistrationRequest;
import tz.bot.repository.CustomerRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRegistrationServiceTest {
    @Mock
    private CustomerRepository customerRepository;

//    private CustomerRepository customerRepository1= Mockito.mock(CustomerRepository.class);

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    private CustomerRegistrationService underTest;

   @BeforeEach
   void setUp(){
        MockitoAnnotations.initMocks(this); //This class whatever are @Mock objects initialize them
        underTest=new CustomerRegistrationService(customerRepository);
   }

    @Test
    void itShouldSaveNewCustomer() {
        //Given
        //... a phone number and a customer
        String phoneNumber="000099";
        Customer customer=new Customer(UUID.randomUUID(),"Mary",phoneNumber);
        //... a request
        CustomerRegistrationRequest request=new CustomerRegistrationRequest(customer);

        // ... No customer for given phone number is passed -- Mocked
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        //When
        underTest.registerNewCustomer(request);
        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue=customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualToComparingFieldByField(customer);


    }

    @Test
    void itShouldSaveNewCustomerWhenIdIsNull() {
        //Given
        //... a phone number and a customer
        String phoneNumber="000099";
        Customer customer=new Customer(null,"Mary",phoneNumber);
        //... a request
        CustomerRegistrationRequest request=new CustomerRegistrationRequest(customer);

        // ... No customer for given phone number is passed -- Mocked
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());
        //When

        underTest.registerNewCustomer(request);

        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue=customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualToIgnoringGivenFields(customer,"id");
//        assertThat(customerArgumentCaptorValue.getId()).isNotNull();
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        //Given
        //... a phone number and a customer
        String phoneNumber="000099";
        Customer customer=new Customer(UUID.randomUUID(),"Mary",phoneNumber);
        //... a request
        CustomerRegistrationRequest request=new CustomerRegistrationRequest(customer);

        // ... Existing customer for given phone number is passed -- Mocked
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));


        //When

        underTest.registerNewCustomer(request);
        //Then

        then(customerRepository).should(never()).save(any());
    }

    @Test
    void itShouldThrowAnExceptionWhenPhoneNumberIsTaken() {
        //Given
        //... a phone number and a two customers
        String phoneNumber="000099";
        Customer customerOne=new Customer(UUID.randomUUID(),"Mary",phoneNumber);
        Customer customerTwo=new Customer(UUID.randomUUID(),"Joseph",phoneNumber);

        //... a request
        CustomerRegistrationRequest request=new CustomerRegistrationRequest(customerOne);

        //... A different customer which has same phone number is returned
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customerTwo));
        //When
        //Then
        assertThatThrownBy(()->underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("phone number [%s] is taken",phoneNumber));

        then(customerRepository).should(never()).save(any(Customer.class));
    }

}