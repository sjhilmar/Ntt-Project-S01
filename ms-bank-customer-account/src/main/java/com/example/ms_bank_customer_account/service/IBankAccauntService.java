package com.example.ms_bank_customer_account.service;


import com.example.ms_bank_customer_account.model.AccountType;
import com.example.ms_bank_customer_account.model.BankAccount;
import com.example.ms_bank_customer_account.model.ClientType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccauntService {

    Flux<BankAccount> getAllBankAccounts();
    Flux<BankAccount> findByClientTypeAndAccountType(ClientType clientType, AccountType accountType);
    Flux<BankAccount>findBankAccountByCustomerID(String customerID);
    Mono<BankAccount> getAccountById(String id);
    Mono<BankAccount> createBankAccount(BankAccount bankAccount);
    Mono<BankAccount> updateBankAccount(String id, BankAccount bankAccount);
    Mono<Void> deleteBankAccount(String id);
}
