package tz.bot.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tz.bot.repository.CustomerRepository;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRegistrationServiceTest {
    @Mock
    private CustomerRepository customerRepository;

//    private CustomerRepository customerRepository1= Mockito.mock(CustomerRepository.class);

    private CustomerRegistrationService underTest;

   @BeforeEach
   void setUp(){
        MockitoAnnotations.initMocks(this); //This class whatever are @Mock objects initialize them
        underTest=new CustomerRegistrationService(customerRepository);
   }

}