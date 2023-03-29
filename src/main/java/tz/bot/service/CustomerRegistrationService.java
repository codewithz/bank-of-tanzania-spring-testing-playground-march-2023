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

        Customer customer=customerRepository.save(request.getCustomer());
        System.out.println("Created Customer:"+customer.getName());
        System.out.println("Created Customer:"+customer.getId());

    }
}
