package com.example.ms_bank_customer_credit.service;

import com.example.ms_bank_customer_credit.model.CreditProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditProductService {

    Flux<CreditProduct> getAllCreditProducts();
    Mono<CreditProduct> getCreditProductById(String id);
    Flux<CreditProduct> findCreditProductByCustomerId(String customerId);
    Mono<CreditProduct> createCreditProduct(CreditProduct creditProduct);
    Mono<CreditProduct> updateCreditProduct(String id, CreditProduct creditProduct);
    Mono<Void> deleteCreditProductById(String id);
}