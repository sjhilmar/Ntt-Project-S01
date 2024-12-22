package com.example.ms_bank_customer.controller;

import com.example.ms_bank_customer.model.Customer;
import com.example.ms_bank_customer.service.impl.CustomerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Flux<Customer> getAllCustomer(){
//        log.info("getting all customers");
//        log.debug(HttpStatus.OK.toString());
        return customerService.getAllCustomer();
    }

    @GetMapping("/{id}")
    public Mono<Customer> getCustomerById(@PathVariable String id){
//        log.info("getting customer with id: {}",id);
//        log.debug(HttpStatus.OK.toString() + " " + id );
        return customerService.getCustomerById(id);
    }

    @GetMapping("/documentNumber/{documentNumber}")
    public Mono<Customer> getCustomerByDocumentNumber(@PathVariable String documentNumber){
//        log.info("getting customer with documentNumber: {}",documentNumber);
//        log.debug(HttpStatus.OK.toString() + " " + documentNumber);
        return customerService.getCustomerByDocumentNumber(documentNumber);
    }

    @PostMapping
    public Mono<ResponseEntity<Customer>> createCustomer(@RequestBody Customer customer){

//        log.info("creating customer: {}",customer);
//        log.debug(customer.toString());
        return customerService.createCustomer(customer)
                .map(createdCustomer ->ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer))
                .onErrorResume(e ->{
                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null));
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Customer>> updateCustomer(@PathVariable String id,@RequestBody Customer customer){
//        log.info("updating customer with id: {}",id);
//        log.debug(customer.toString());
        return customerService.updateCustomer(id,customer)
                .map(ResponseEntity::ok)
                .onErrorResume(e ->{
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
                });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable String id){
//        log.info("deleting customer with id: {}",id);
//        log.debug(HttpStatus.NO_CONTENT.toString() + " " + id);
        return customerService.deleteCustomerById(id);

    }

}
