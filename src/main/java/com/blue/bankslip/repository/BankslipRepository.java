package com.blue.bankslip.repository;

import com.blue.bankslip.domain.Bankslip;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDate;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "Bankslips", path = "bankslip")
public interface BankslipRepository extends CrudRepository<Bankslip, UUID> {

    @RestResource(path = "payments")
    @Query("update #{#entityName} e set e.paymentDate=?1, e.status='PAID' where e.id=?2")
    @Modifying
    void pay(final LocalDate paymentDate, UUID id);

    @Override
    @Query("update #{#entityName} e set e.status='CANCELED' where e.id=?1 AND e.status='PENDING'")
    @Modifying
    void delete(UUID uuid);

}


