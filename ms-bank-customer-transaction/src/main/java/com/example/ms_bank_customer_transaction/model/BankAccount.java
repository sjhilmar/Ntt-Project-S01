package com.example.ms_bank_customer_transaction.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "bankAccounts")
public class BankAccount {
    @Id
    private String id;

    @NotEmpty(message = "Se requiere el código del cliente")
    private String customerId;

    @NotEmpty(message = "Se requiere número de cuenta")
    private String accountNumber;

    @NotEmpty(message = "Se requiere nombre del titular")
    private String accountHolderName;

    @NotNull(message = "El saldo inicial es requerido")
    private BigDecimal balance;

    private List<String> authorizedSigners; // For business accounts
    private List<String> holders; // For business accounts

    @NotNull(message = "Se requiere tipo de cuenta")
    private AccountType accountType;

    @NotNull(message = "Se requiere tipo de cliente")
    private ClientType clientType;
}
