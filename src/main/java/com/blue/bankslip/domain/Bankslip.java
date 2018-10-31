package com.blue.bankslip.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;

@Data
@RequiredArgsConstructor
@Entity
public class Bankslip implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @JsonProperty(value = "due_date")
    private LocalDate dueDate;

    @JsonProperty(value = "payment_date")
    private LocalDate paymentDate;

    @NotNull
    @JsonProperty("total_in_cents")
    private BigDecimal totalCents;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private BigDecimal fine;

    @NotNull
    private String customer;

    @Enumerated(EnumType.STRING)
    private BankslipStatus status = BankslipStatus.PENDING;


    public BigDecimal getTotalCents() {
        this.fine = calculateFine();
        return this.totalCents.add(this.fine);
    }

    private BigDecimal calculateFine() {
        if (DAYS.between(dueDate, LocalDate.now()) > 10) {
            return this.totalCents.multiply(new BigDecimal("0.01"));
        } else if (DAYS.between(dueDate, LocalDate.now()) > 1) {
            return this.totalCents.multiply(new BigDecimal("0.005"));
        } else {
            return BigDecimal.ZERO;
        }
    }

}
