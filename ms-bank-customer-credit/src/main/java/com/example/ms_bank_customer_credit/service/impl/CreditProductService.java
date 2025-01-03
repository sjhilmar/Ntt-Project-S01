package com.example.ms_bank_customer_credit.service.impl;

import com.example.ms_bank_customer_credit.model.ClientType;
import com.example.ms_bank_customer_credit.model.CreditProduct;
import com.example.ms_bank_customer_credit.model.CreditType;
import com.example.ms_bank_customer_credit.repository.CreditProductRepository;
import com.example.ms_bank_customer_credit.service.ICreditProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditProductService implements ICreditProductService {
    @Autowired
    private CreditProductRepository creditProductRepository;

    @Override
    public Flux<CreditProduct> getAllCreditProducts() {
        return creditProductRepository.findAll();
    }

    @Override
    public Mono<CreditProduct> getCreditProductById(String id) {
        return creditProductRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Credit Product not found")));
    }

    @Override
    public Flux<CreditProduct> findCreditProductByCustomerId(String customerId) {
        return creditProductRepository.findCreditProductByCustomerId(customerId);
    }

    @Override
    public Mono<CreditProduct> createCreditProduct(CreditProduct creditProduct) {


        return creditProductRepository.findCreditProductByCustomerId(creditProduct.getCustomerId())
                .collectList()
                .flatMap(existingCredits->{
                   if (creditProduct.getClientType()==ClientType.PERSONAL){
                       long personalCreditCount = existingCredits.stream().filter(credit -> credit.getClientType()==ClientType.PERSONAL).count();
                       long tarjetaCreditoCount = existingCredits.stream().filter(credit-> credit.getCreditType() ==CreditType.TARJETACREDITO).count();

                       if (creditProduct.getCreditType()== CreditType.PERSONAL && personalCreditCount >0){
                           return Mono.error(new RuntimeException("El cliente personal solo puede tener un crédito personal"));
                       }
                       if (creditProduct.getCreditType()==CreditType.TARJETACREDITO && tarjetaCreditoCount>0){
                           return Mono.error(new RuntimeException("El cliente personal solo puede tener una tarjeta de crédito"));
                       }
                       if(creditProduct.getCreditType() ==CreditType.EMPRESARIAL){
                           return Mono.error(new RuntimeException("El cliente personal no puede tener un crédito empresarial"));
                       }
                       return creditProductRepository.save(creditProduct);

                   }else if (creditProduct.getClientType()==ClientType.EMPRESARIAL){
                       if (creditProduct.getCreditType() ==CreditType.PERSONAL){
                           return Mono.error(new RuntimeException("El cliente empresarial no puede tener un crédito personal"));
                       }
                       if (creditProduct.getHolders()==null || creditProduct.getHolders().isEmpty()){
                           return Mono.error(new RuntimeException("El cliente empresarial debe tener al menos un titular"));
                       }
                       return creditProductRepository.save(creditProduct);
                   }else {
                       return Mono.error(new RuntimeException("No existe el tipo de Cliente"));
                   }

                });
    }

    @Override
    public Mono<CreditProduct> updateCreditProduct(String id, CreditProduct creditProduct) {
        return creditProductRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Credit product not found")))
                .flatMap(existingProduct -> {
                    existingProduct.setAccountHolderName(creditProduct.getAccountHolderName());
                    existingProduct.setCreditAmount(creditProduct.getCreditAmount());
                    existingProduct.setAuthorizedSigners(creditProduct.getAuthorizedSigners());
                    existingProduct.setHolders(creditProduct.getHolders());
                    existingProduct.setCreditType(creditProduct.getCreditType());
                    existingProduct.setClientType(creditProduct.getClientType());
                    return creditProductRepository.save(existingProduct);
                });
    }

    @Override
    public Mono<Void> deleteCreditProductById(String id) {
        return creditProductRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Credit product not found")))
                .flatMap(existingProduct -> creditProductRepository.deleteById(id));
    }
}
