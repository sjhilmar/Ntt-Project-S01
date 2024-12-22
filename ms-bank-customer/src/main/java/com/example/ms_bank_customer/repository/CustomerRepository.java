package com.example.ms_bank_customer.repository;

import com.example.ms_bank_customer.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository  extends ReactiveCrudRepository<Customer,String> {

    Mono<Customer> findCustomerByDocumentNumber(String documentNumber);
}
