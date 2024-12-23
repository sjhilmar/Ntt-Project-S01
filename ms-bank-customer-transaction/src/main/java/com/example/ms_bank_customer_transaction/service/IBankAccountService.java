package com.example.ms_bank_customer_transaction.service;

import com.example.ms_bank_customer_transaction.model.BankAccount;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface IBankAccountService {
    Flux<BankAccount> getallBankAccounts();
    Mono<BankAccount> getBankAccountById(String id);
    Mono<BankAccount> createBankAccount(BankAccount bankAccount);
    Mono<BankAccount> updateBankAccount(String id, BankAccount bankAccount);
    Mono<Void> deleteBankAccountById(String id);
}
