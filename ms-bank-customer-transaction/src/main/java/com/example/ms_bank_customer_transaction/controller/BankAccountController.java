package com.example.ms_bank_customer_transaction.controller;

import com.example.ms_bank_customer_transaction.model.BankAccount;
import com.example.ms_bank_customer_transaction.service.IBankAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bank-account")
public class BankAccountController {
    @Autowired
    private IBankAccountService bankAccountService;

    @GetMapping
    public ResponseEntity<Flux<BankAccount>> getAllBankAccounts() {
        return ResponseEntity.ok(bankAccountService.getallBankAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<BankAccount>> getBankAccountById(@PathVariable String id) {
        return ResponseEntity.ok(bankAccountService.getBankAccountById(id));
    }

    @PostMapping
    public ResponseEntity<Mono<BankAccount>> createBankAccount(@Valid @RequestBody BankAccount bankAccount) {
        return ResponseEntity.status(201).body(bankAccountService.createBankAccount(bankAccount));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mono<BankAccount>> updateBankAccount(@PathVariable String id, @Valid @RequestBody BankAccount bankAccount) {
        return ResponseEntity.ok(bankAccountService.updateBankAccount(id, bankAccount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable String id) {
        bankAccountService.deleteBankAccountById(id).subscribe();
        return ResponseEntity.noContent().build();
    }


}
