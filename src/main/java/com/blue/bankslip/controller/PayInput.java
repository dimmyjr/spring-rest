package com.blue.bankslip.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class PayInput {
    @JsonProperty(value = "payment_date")
    private LocalDate paymentDate;
}
