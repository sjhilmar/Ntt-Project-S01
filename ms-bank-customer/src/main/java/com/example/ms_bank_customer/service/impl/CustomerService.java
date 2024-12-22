package com.example.ms_bank_customer.service.impl;

import com.example.ms_bank_customer.model.Customer;
import com.example.ms_bank_customer.repository.CustomerRepository;
import com.example.ms_bank_customer.service.ICustomerService;
import com.example.ms_bank_customer.util.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class CustomerService implements ICustomerService {
    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public Flux<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public Mono<Customer> getCustomerById(String id) {
        return customerRepository.findById(id);
    }

    @Override
    public Mono<Customer> getCustomerByDocumentNumber(String documentNumber) {
        return customerRepository.findCustomerByDocumentNumber(documentNumber);
    }

    @Override
    public Mono<Customer> createCustomer(Customer customer) {

        if (customer.getDocumentNumber()== null || customer.getDocumentNumber().isBlank()) {
            return Mono.error(new IllegalStateException("El numero de documento no puede estar vacío"));
        }
        return customerRepository.save(customer);
        }

    @Override
    public Mono<Customer> updateCustomer(String id, Customer customer) {
        return customerRepository.findById(id).flatMap(existingCustomer ->{
           existingCustomer.setDocumentNumber(customer.getDocumentNumber());
           existingCustomer.setCompanyName(customer.getCompanyName());
           existingCustomer.setName(customer.getName());
           existingCustomer.setEmail(customer.getEmail());
           existingCustomer.setPhoneNumber(customer.getPhoneNumber());
           existingCustomer.setCustomerType(customer.getCustomerType());
           return customerRepository.save(existingCustomer);
        });
    }

    @Override
    public Mono<Void> deleteCustomerById(String id) {
        return customerRepository.deleteById(id);
    }
}
