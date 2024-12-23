package com.example.ms_bank_customer_transaction.service.impl;

import com.example.ms_bank_customer_transaction.model.Transaction;
import com.example.ms_bank_customer_transaction.repository.BankAccountRepository;
import com.example.ms_bank_customer_transaction.repository.CreditProductRepository;
import com.example.ms_bank_customer_transaction.repository.TransactionRepository;
import com.example.ms_bank_customer_transaction.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CreditProductRepository creditProductRepository;


    @Override
    public Flux<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Flux<Transaction> getTransactionsByProductId(String productId) {
        return transactionRepository.findByProductId(productId);
    }

    @Override
    public Mono<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction not found")));
    }

    @Override
    public Mono<Transaction> createTransaction(Transaction transaction) {
        transaction.setTransactionDate(LocalDateTime.now());

        switch (transaction.getProductType()) {
            case CUENTABANCARIA:
                return handleBankAccountTransaction (transaction);
            case CREDITO:
            case TARJETACREDITO:
                return handleCreditProductTransaction(transaction);
            default:
                return Mono.error(new RuntimeException("Tipo de producto no válido"));
        }
    }

    @Override
    public Mono<Transaction> handleBankAccountTransaction(Transaction transaction) {
        return bankAccountRepository.findById(transaction.getProductId())
                .switchIfEmpty(Mono.error(new RuntimeException("Cuenta bancaria no encontrada")))
                .flatMap(bankAccount -> {
                    BigDecimal newBalance;
                    switch (transaction.getTransactionType()) {
                        case DEPOSITO:
                            newBalance = bankAccount.getBalance().add(transaction.getAmount());
                            break;
                        case RETIRO:
                            if (bankAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                                return Mono.error(new RuntimeException("Saldo insuficiente"));
                            }
                            newBalance = bankAccount.getBalance().subtract(transaction.getAmount());
                            break;
                        default:
                            return Mono.error(new RuntimeException("Tipo de transacción no válido para cuentas bancarias"));
                    }
                    bankAccount.setBalance(newBalance);
                    return bankAccountRepository.save(bankAccount).then(transactionRepository.save(transaction));
                });
    }

    @Override
    public Mono<Transaction> handleCreditProductTransaction(Transaction transaction) {
        return creditProductRepository.findById(transaction.getProductId())
                .switchIfEmpty(Mono.error(new RuntimeException("Producto de crédito no encontrado")))
                .flatMap(creditProduct -> {
                    switch (transaction.getTransactionType()) {
                        case PAGO:
                            creditProduct.setCreditAmount(creditProduct.getCreditAmount().subtract(transaction.getAmount()));
                            break;
                        case CONSUMO:
                            creditProduct.setCreditAmount(creditProduct.getCreditAmount().add(transaction.getAmount()));
                            break;
                        default:
                            return Mono.error(new RuntimeException("Tipo de transacción no válido para productos de crédito"));
                    }
                    return creditProductRepository.save(creditProduct).then(transactionRepository.save(transaction));
                });
    }

    @Override
    public Mono<Transaction> updateTransaction(String id, Transaction transaction) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction not found")))
                .flatMap(existingTransaction -> {
                    existingTransaction.setAmount(transaction.getAmount());
                    existingTransaction.setProductId(transaction.getProductId());
                    existingTransaction.setProductType(transaction.getProductType());
                    existingTransaction.setTransactionType(transaction.getTransactionType());
                    existingTransaction.setTransactionDate(transaction.getTransactionDate());
                    return transactionRepository.save(existingTransaction);
                });
    }

    @Override
    public Mono<Void> deleteTransactionById(String id) {
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction not found")))
                .flatMap(existingTransaction -> transactionRepository.deleteById(id));
    }

}
