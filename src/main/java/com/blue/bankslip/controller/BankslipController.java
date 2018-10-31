package com.blue.bankslip.controller;

import com.blue.bankslip.URLMapping;
import com.blue.bankslip.repository.BankslipRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(URLMapping.ROOT + URLMapping.BANKSLIP)
@Api(value = "Bankslips", description = "Operations to bankslips")
public class BankslipController {

    private final BankslipRepository repository;

    public BankslipController(@Autowired final BankslipRepository repository) {
        this.repository = repository;
    }

    @ApiOperation(value = "Pay bankslip", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Bankslip not found with the specified id")
    })
    @PostMapping(path = "{id}/payments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity pay(@PathVariable("id") UUID id, @RequestBody PayInput input) {
        if (!repository.exists(id)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        repository.pay(id, input.getPaymentDate());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
