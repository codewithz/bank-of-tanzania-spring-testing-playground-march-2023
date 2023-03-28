package tz.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tz.bot.model.Customer;
import tz.bot.model.CustomerRegistrationRequest;
import tz.bot.repository.CustomerRepository;

import java.util.Optional;

@Service
public class CustomerRegistrationService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request){
            //1. PhoneNumber is taken
            //2. if taken lets check if its belong to the same customer
                //2.1 if yes return
                //2.2 throw an exception
            //3. Save Customer

        String phoneNumber=request.getCustomer().getPhoneNumber();
        Optional<Customer> customerOptional=customerRepository.
                selectCustomerByPhoneNumber(phoneNumber);

        if(customerOptional.isPresent()){
            Customer customer=customerOptional.get();
            if(customer.getName().equals(request.getCustomer().getName())){
                return;
            }
            throw  new IllegalStateException(String.format("phone number [%s] is taken",phoneNumber));
        }
        customerRepository.save(request.getCustomer());

    }
}
