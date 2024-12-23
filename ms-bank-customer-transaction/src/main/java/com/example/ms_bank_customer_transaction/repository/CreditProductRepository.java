package com.example.ms_bank_customer_transaction.repository;

import com.example.ms_bank_customer_transaction.model.CreditProduct;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CreditProductRepository  extends ReactiveMongoRepository<CreditProduct, String> {

    Flux<CreditProduct> findByCustomerId(String customerId);

}
