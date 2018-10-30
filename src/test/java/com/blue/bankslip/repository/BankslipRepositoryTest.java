package com.blue.bankslip.repository;

import com.blue.bankslip.BankApplication;
import com.blue.bankslip.domain.Bankslip;
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

import static org.hamcrest.Matchers.is;
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
                        "  \"total_in_cents\" : \"100000\" ,\n" +
                        "  \"customer\" : \"Trillian Company\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotCreateBankslip() throws Exception {
        mvc.perform(post("/rest/bankslip")
                .content("{\n" +
                        "  \"total_in_cents\" : \"100000\" ,\n" +
                        "  \"customer\" : \"Trillian Company\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetBankslip() throws Exception {
        final String customer = "Joselito Moraes";
        final UUID uuid = createBankslip(customer);

        mvc.perform(get("/rest/bankslip/" + uuid.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(uuid.toString())))
                .andExpect(jsonPath("$.customer", is(customer)));
    }

    @Test
    public void shouldNotGetBankslip() throws Exception {
        final String customer = "Joselito Moraes";
        final UUID uuid = UUID.randomUUID();

        mvc.perform(get("/rest/bankslip/" + uuid.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private UUID createBankslip(final String customer) {
        final Bankslip bankslip = new Bankslip();
        bankslip.setCustomer(customer);
        bankslip.setDueDate(LocalDate.now());
        bankslip.setTotalCents(BigDecimal.TEN);

        repository.save(bankslip);
        return bankslip.getId();
    }

}