package com.example.ms_bank_customer_transaction.repository;

import com.example.ms_bank_customer_transaction.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> findByProductId(String customerId);
}
