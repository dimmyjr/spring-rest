package com.blue.bankslip.domain;

import com.blue.bankslip.listener.BankslipListener;
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

@Data
@RequiredArgsConstructor
@Entity
@EntityListeners(BankslipListener.class)
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

}
