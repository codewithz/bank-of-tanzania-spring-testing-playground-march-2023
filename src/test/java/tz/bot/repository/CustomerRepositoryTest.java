package tz.bot.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import tz.bot.model.Customer;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        //Given
        UUID id=UUID.randomUUID();
        String phoneNumber="12345";
        Customer customer=new Customer(id,"Mary",phoneNumber);
        //When

        underTest.save(customer);
        //Then
        Optional<Customer> optionalCustomer=underTest.selectCustomerByPhoneNumber(phoneNumber);
        Customer selectedCustomer=optionalCustomer.get();
        assertThat(optionalCustomer).isPresent();
        assertThat(selectedCustomer).isEqualToComparingFieldByField(customer);
    }

    @Test
    void itShouldNotSelectCustomerByPhoneNumberWhenPhoneNumberDoesNotExist() {
        //Given
        String phoneNumber="54321";
        //When
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
        //Then
        assertThat(optionalCustomer).isNotPresent();
    }

    @Test
    void itShouldSaveCustomer() {
        //Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Alex", "00000");

        //When
        underTest.save(customer);
        //Then

        Optional<Customer> optionalCustomer=underTest.findById(id);
        assertThat(optionalCustomer).isPresent();
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c->{
                   assertThat(c.getId()).isEqualTo(id);
                   assertThat(c.getName()).isEqualTo("Alex");
                   assertThat(c.getPhoneNumber()).isEqualTo("00000");
                });
        assertThat(optionalCustomer.get()).isEqualToComparingFieldByField(customer);
    }

    @Test
    void itShouldNotSaveCustomerWhenNameIsNull() {
        //Given
        UUID id=UUID.randomUUID();
        Customer customer=new Customer(id,null,"11111");
        //When

        //Then
        assertThatThrownBy(()->underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : tz.bot.model.Customer.name")
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberIsNull() {
        //Given
        UUID id=UUID.randomUUID();
        Customer customer=new Customer(id,"Alex",null);
        //When

        //Then
        assertThatThrownBy(()->underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : tz.bot.model.Customer.phoneNumber")
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}