package com.example.ms_bank_customer_transaction.service.impl;

import com.example.ms_bank_customer_transaction.model.AccountType;
import com.example.ms_bank_customer_transaction.model.BankAccount;
import com.example.ms_bank_customer_transaction.model.ClientType;
import com.example.ms_bank_customer_transaction.repository.BankAccountRepository;
import com.example.ms_bank_customer_transaction.service.IBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountService implements IBankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public Flux<BankAccount> getallBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public Mono<BankAccount> getBankAccountById(String id) {
        return bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("BankAccount not found")));
    }

    @Override
    public Mono<BankAccount> createBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.findByCustomerId(bankAccount.getCustomerId())
                .collectList()
                .flatMap(existingAccounts -> {
                    if (bankAccount.getClientType() == ClientType.PERSONAL) {
                        long savingsCount = existingAccounts.stream()
                                .filter(account -> account.getAccountType() == AccountType.AHORRO)
                                .count();
                        long checkingCount = existingAccounts.stream()
                                .filter(account -> account.getAccountType() == AccountType.CORRIENTE)
                                .count();

                        if (bankAccount.getAccountType() == AccountType.AHORRO && savingsCount > 0) {
                            return Mono.error(new RuntimeException("El cliente personal solo puede tener una cuenta de ahorros"));
                        }
                        if (bankAccount.getAccountType() == AccountType.CORRIENTE && checkingCount > 0) {
                            return Mono.error(new RuntimeException("El cliente personal solo puede tener una cuenta corriente"));
                        }
                    } else if (bankAccount.getClientType() == ClientType.EMPRESARIAL) {
                        if (bankAccount.getAccountType()==AccountType.AHORRO || bankAccount.getAccountType()==AccountType.PLAZOFIJO) {
                            return Mono.error(new RuntimeException("El cliente empresarial no puede tener cuentas de ahorro ni de plazo fijo"));
                        }
                        if (bankAccount.getHolders() == null || bankAccount.getHolders().isEmpty()) {
                            return Mono.error(new RuntimeException("Las cuentas bancarias empresariales deben tener al menos un titular"));
                        }
                    }
                    return bankAccountRepository.save(bankAccount);
                });
    }

    @Override
    public Mono<BankAccount> updateBankAccount(String id, BankAccount bankAccount) {
        return bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Bank account not found")))
                .flatMap(existingAccount -> {
                    existingAccount.setAccountNumber(bankAccount.getAccountNumber());
                    existingAccount.setAccountHolderName(bankAccount.getAccountHolderName());
                    existingAccount.setBalance(bankAccount.getBalance());
                    existingAccount.setAuthorizedSigners(bankAccount.getAuthorizedSigners());
                    existingAccount.setHolders(bankAccount.getHolders());
                    existingAccount.setAccountType(bankAccount.getAccountType());
                    existingAccount.setClientType(bankAccount.getClientType());
                    return bankAccountRepository.save(existingAccount);
                });
    }

    @Override
    public Mono<Void> deleteBankAccountById(String id) {
        return bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Bank account not found")))
                .flatMap(existingAccount -> bankAccountRepository.deleteById(id));
    }
}
