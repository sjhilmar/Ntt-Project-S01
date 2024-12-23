package com.example.ms_bank_customer_account.repository;

import com.example.ms_bank_customer_account.model.AccountType;
import com.example.ms_bank_customer_account.model.BankAccount;
import com.example.ms_bank_customer_account.model.ClientType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

    Flux<BankAccount> findByClientTypeAndAccountType(ClientType clientType, AccountType accountType);
    Flux<BankAccount> findBankAccountByCustomerId(String customerId);

}
