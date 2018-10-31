package com.blue.bankslip.listener;

import com.blue.bankslip.domain.Bankslip;

import javax.persistence.PostLoad;
import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class BankslipListener {

    private static final String DELAYED_MORE_TAX = "0.01";
    private static final String DELAYED_LESS_TAX = "0.005";
    private static final int DELAYED_DAYS = 10;

    @PostLoad
    public void BankslipPostLoad(Bankslip bankslip) {
        this.calculateFine(bankslip);
    }

    private void calculateFine(final Bankslip bankslip) {
        if (bankslip.getDueDate() != null && DAYS.between(bankslip.getDueDate(), LocalDate.now()) > DELAYED_DAYS) {
            bankslip.setFine(bankslip.getTotalCents().multiply(new BigDecimal(DELAYED_MORE_TAX)));
        } else if (bankslip.getDueDate() != null && DAYS.between(bankslip.getDueDate(), LocalDate.now()) > 1) {
            bankslip.setFine(bankslip.getTotalCents().multiply(new BigDecimal(DELAYED_LESS_TAX)));
        } else {
            bankslip.setFine(BigDecimal.ZERO);
        }

        bankslip.setTotalCents(bankslip.getTotalCents().add(bankslip.getFine()));
    }

}
