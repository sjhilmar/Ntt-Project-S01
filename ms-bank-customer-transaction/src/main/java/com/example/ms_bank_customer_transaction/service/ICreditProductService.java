package com.example.ms_bank_customer_transaction.service;

import com.example.ms_bank_customer_transaction.model.CreditProduct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ICreditProductService {
    Flux<CreditProduct> getAllCreditProducts();
    Mono<CreditProduct> getCreditProductById(String id);
    Mono<CreditProduct> createCreditProduct(CreditProduct creditProduct);
    Mono<CreditProduct> updateCreditProduct(String id, CreditProduct creditProduct);
    Mono<Void> deleteCreditProductById(String id);


}
