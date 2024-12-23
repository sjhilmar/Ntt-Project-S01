package com.example.ms_bank_customer_transaction.controller;

import com.example.ms_bank_customer_transaction.model.Transaction;
import com.example.ms_bank_customer_transaction.service.ITransactionService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping
    public Flux<Transaction> getAllTransactions() {
       log.info("Consultando todas las transacciones");
       log.debug("Consultando todas las transacciones");
        return transactionService.getAllTransactions();
    }

    @GetMapping("/product/{productId}")
    public Flux<Transaction> getTransactionsByProductId(@PathVariable String productId) {
        log.info("Consultando todas las transacciones del producto con id {}",productId);
        log.debug("Consultando todas las transacciones del producto con id {}",productId);
        return transactionService.getTransactionsByProductId(productId);
    }

    @PostMapping
    public Mono<ResponseEntity<Transaction>> createTransaction(@Valid @RequestBody Transaction transaction) {
        log.info("Creando transacción {}",transaction);
        log.debug("Creando transacción {}",transaction);
        return transactionService.createTransaction(transaction)
                .map(savedTransaction -> ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Transaction>> updateTransaction(@PathVariable String id, @Valid @RequestBody Transaction transaction) {
        log.info("Actualizando transacción {}",transaction);
        log.debug("Actualizando transacción {}",transaction);
        return transactionService.updateTransaction(id, transaction)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTransaction(@PathVariable String id) {
        log.info("Eliminando Tranascción{}",id);
        return transactionService.deleteTransactionById(id);
    }
}
