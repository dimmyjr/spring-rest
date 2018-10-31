package com.blue.bankslip.repository;

import com.blue.bankslip.URLMapping;
import com.blue.bankslip.domain.Bankslip;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "Bankslips", path = URLMapping.BANKSLIP)
public interface BankslipRepository extends CrudRepository<Bankslip, UUID> {

    @RestResource(exported = false)
    @Transactional
    @Query("update #{#entityName} e set e.paymentDate=?2, e.status='PAID' where e.id=?1")
    @Modifying
    void pay(final UUID id, final LocalDate paymentDate);

    @Override
    @ApiOperation(value = "Cancel bankslip", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Bankslip canceled"),
            @ApiResponse(code = 404, message = "Bankslip not found with the specified id")
    })
    @Query("update #{#entityName} e set e.status='CANCELED' where e.id=?1 AND e.status='PENDING'")
    @Modifying
    void delete(UUID uuid);

    @Override
    @ApiOperation(value = "Create bankslip", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Bankslip created"),
            @ApiResponse(code = 400, message = "Bankslip not provided in the request body"),
            @ApiResponse(code = 422, message = "Invalid bankslip provided.The possible reasons are: A field of the provided bankslip was null or with invalid values")
    })
    <S extends Bankslip> S save(S s);

    @Override
    @ApiOperation(value = "Show details of bankslip", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Bankslip not found with the specified id")
    })
    Bankslip findOne(UUID uuid);

    @Override
    @ApiOperation(value = "List of bankslip", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    Iterable<Bankslip> findAll();
}


