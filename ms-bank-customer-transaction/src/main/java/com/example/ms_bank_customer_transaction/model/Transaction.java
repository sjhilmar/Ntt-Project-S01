package com.example.ms_bank_customer_transaction.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;

    @NotEmpty(message = "Se requiere el ID del producto")
    private String productId;

    private ProductType productType;

    private TransactionType transactionType;

    @NotNull(message = "El monto no puede ser nulo")
    private BigDecimal amount;

    @NotNull(message = "La fecha de la transacción no puede ser nula")
    private LocalDateTime transactionDate;
}
