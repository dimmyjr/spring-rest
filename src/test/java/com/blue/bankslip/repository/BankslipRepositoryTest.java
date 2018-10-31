package com.blue.bankslip.repository;

import com.blue.bankslip.BankApplication;
import com.blue.bankslip.domain.Bankslip;
import com.blue.bankslip.domain.BankslipStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = BankApplication.class)
@AutoConfigureMockMvc
public class BankslipRepositoryTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BankslipRepository repository;

    @Test
    public void shouldCreateBankslip() throws Exception {
        mvc.perform(post("/rest/bankslip")
                .content("{\n" +
                        "  \"due_date\": \"2018-01-01\",\n" +
                        "  \"total_in_cents\" : \"10\" ,\n" +
                        "  \"customer\" : \"Trillian Company\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotCreateBankslip() throws Exception {
        mvc.perform(post("/rest/bankslip")
                .content("{\n" +
                        "  \"total_in_cents\" : \"10\" ,\n" +
                        "  \"customer\" : \"Trillian Company\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetBankslip() throws Exception {
        final String customer = "Joselito Moraes Get";
        final UUID uuid = createBankslip(customer, LocalDate.now());

        mvc.perform(get("/rest/bankslip/" + uuid.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(uuid.toString())))
                .andExpect(jsonPath("$.customer", is(customer)))
                .andExpect(jsonPath("$.total_in_cents").value("10.0"));

    }

    @Test
    public void shouldGetBankslipWithLateMinus10Days() throws Exception {
        final String customer = "Joselito Moraes 5";
        final UUID uuid = createBankslip(customer, LocalDate.now().minusDays(9));

        mvc.perform(get("/rest/bankslip/" + uuid.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(uuid.toString())))
                .andExpect(jsonPath("$.customer", is(customer)))
                .andExpect(jsonPath("$.total_in_cents").value("10.05"));
    }

    @Test
    public void shouldGetBankslipWithLateMore10Days() throws Exception {
        final String customer = "Joselito Moraes 11";
        final UUID uuid = createBankslip(customer, LocalDate.now().minusDays(11));

        mvc.perform(get("/rest/bankslip/" + uuid.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(uuid.toString())))
                .andExpect(jsonPath("$.customer", is(customer)))
                .andExpect(jsonPath("$.total_in_cents").value("10.1"));
    }

    @Test
    public void shouldNotGetBankslip() throws Exception {
        final UUID uuid = UUID.randomUUID();

        mvc.perform(get("/rest/bankslip/" + uuid.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldPayBankslip() throws Exception {
        final String customer = "Joselito Moraes Pay";
        final UUID uuid = createBankslip(customer, LocalDate.now());

        mvc.perform(post("/rest/bankslip/" + uuid.toString() + "/payments")
                .content("{\n" +
                        "  \"payment_date\" : \"2018-10-30\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        final Bankslip bankslip = repository.findOne(uuid);
        assertThat(bankslip.getPaymentDate(), is(not(nullValue())));
        assertThat(bankslip.getStatus(), equalTo(BankslipStatus.PAID));

    }

    @Test
    public void shouldPayBankslipNotFound() throws Exception {
        final UUID uuid = UUID.randomUUID();

        mvc.perform(post("/rest/bankslip/" + uuid.toString() + "/payments")
                .content("{\n" +
                        "  \"payment_date\" : \"2018-10-30\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCancelBankslip() throws Exception {
        final String customer = "Joselito Moraes Cancel";
        final UUID uuid = createBankslip(customer, LocalDate.now());

        mvc.perform(delete("/rest/bankslip/" + uuid.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        final Bankslip bankslip = repository.findOne(uuid);
        assertThat(bankslip.getStatus(), equalTo(BankslipStatus.CANCELED));

    }

    private UUID createBankslip(final String customer, final LocalDate dueDate) {
        final Bankslip bankslip = new Bankslip();
        bankslip.setCustomer(customer);
        bankslip.setDueDate(dueDate);
        bankslip.setTotalCents(BigDecimal.TEN);

        repository.save(bankslip);
        return bankslip.getId();
    }

}