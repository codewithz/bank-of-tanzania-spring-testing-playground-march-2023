package tz.bot.repository;

import org.springframework.data.repository.CrudRepository;
import tz.bot.model.Payment;

public interface PaymentRepository extends CrudRepository<Payment,Long> {
}
