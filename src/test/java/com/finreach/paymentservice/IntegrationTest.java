package com.finreach.paymentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finreach.paymentservice.api.request.CreateAccount;
import com.finreach.paymentservice.api.request.CreatePayment;
import com.finreach.paymentservice.statistics.Statistics;
import com.finreach.paymentservice.util.TransactionsGenerator;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TransactionsGenerator generator;

    @Test
    public void scenario1() throws Exception {
        final String a1 = createAccount(100d);
        final String a2 = createAccount(0d);

        MvcResult result = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(new CreatePayment(50d, a1, a2))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(50d))
                .andExpect(jsonPath("$.state").value("CREATED"))
                .andExpect(jsonPath("$.sourceAccountId").value(a1))
                .andExpect(jsonPath("$.destinationAccountId").value(a2))
                .andReturn();

        mockMvc.perform(get("/api/v1/account/" + a1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100d))
                .andExpect(jsonPath("$.transactions").isEmpty());

        mockMvc.perform(get("/api/v1/account/" + a2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0d))
                .andExpect(jsonPath("$.transactions").isEmpty());

        final String id = mapper.readTree(result.getResponse().getContentAsString()).findValue("id").asText();
        mockMvc.perform(put("/api/v1/payment/execute/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("EXECUTED"));

        mockMvc.perform(get("/api/v1/account/" + a1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(50d))
                .andExpect(jsonPath("$.transactions").isNotEmpty())
                .andExpect(jsonPath("$.transactions[0].account").value(a1))
                .andExpect(jsonPath("$.transactions[0].amount").value(-50d));

        mockMvc.perform(get("/api/v1/account/" + a2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(50d))
                .andExpect(jsonPath("$.transactions").isNotEmpty())
                .andExpect(jsonPath("$.transactions[0].account").value(a2))
                .andExpect(jsonPath("$.transactions[0].amount").value(50d));

        mockMvc.perform(put("/api/v1/payment/cancel/" + id))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/payment/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("EXECUTED"));
    }

    @Test
    public void scenario2() throws Exception {
        final String a1 = createAccount(0d);
        final String a2 = createAccount(0d);

        MvcResult result = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(new CreatePayment(50d, a1, a2))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(50d))
                .andExpect(jsonPath("$.state").value("CREATED"))
                .andExpect(jsonPath("$.sourceAccountId").value(a1))
                .andExpect(jsonPath("$.destinationAccountId").value(a2))
                .andReturn();

        mockMvc.perform(get("/api/v1/account/" + a1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0d))
                .andExpect(jsonPath("$.transactions").isEmpty());

        mockMvc.perform(get("/api/v1/account/" + a2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0d))
                .andExpect(jsonPath("$.transactions").isEmpty());

        final String id = mapper.readTree(result.getResponse().getContentAsString()).findValue("id").asText();
        mockMvc.perform(put("/api/v1/payment/execute/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("REJECTED"));

        mockMvc.perform(get("/api/v1/account/" + a1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0d))
                .andExpect(jsonPath("$.transactions").isEmpty());

        mockMvc.perform(get("/api/v1/account/" + a2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0d))
                .andExpect(jsonPath("$.transactions").isEmpty());

        mockMvc.perform(put("/api/v1/payment/cancel/" + id))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/payment/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("REJECTED"));
    }

    @Test
    public void scenario3() throws Exception {
        final String a1 = createAccount(0d);
        final String a2 = createAccount(0d);

        MvcResult result = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(new CreatePayment(50d, a1, a2))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(50d))
                .andExpect(jsonPath("$.state").value("CREATED"))
                .andExpect(jsonPath("$.sourceAccountId").value(a1))
                .andExpect(jsonPath("$.destinationAccountId").value(a2))
                .andReturn();

        mockMvc.perform(get("/api/v1/account/" + a1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0d))
                .andExpect(jsonPath("$.transactions").isEmpty());

        mockMvc.perform(get("/api/v1/account/" + a2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0d))
                .andExpect(jsonPath("$.transactions").isEmpty());


        final String id = mapper.readTree(result.getResponse().getContentAsString()).findValue("id").asText();
        mockMvc.perform(put("/api/v1/payment/cancel/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("CANCELED"));

        mockMvc.perform(put("/api/v1/payment/execute/" + id))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/payment/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("CANCELED"));
    }

    @Test
    public void scenario4() throws Exception {
        final String a1 = createAccount(0d);

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(new CreatePayment(50d, a1, a1))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void scenario5() throws Exception {
        final String a1 = createAccount(0d);
        final String a2 = createAccount(0d);

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(new CreatePayment(-50d, a1, a2))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void scenario6() throws Exception {
        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(new CreatePayment(-50d,
                        "nonexistent",
                        "nonexistent"))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void scenario7() throws Exception {
        final String a1 = createAccount(0d);

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(new CreatePayment(-50d, a1, "nonexistent"))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void scenario8() throws Exception {
        mockMvc.perform(put("/api/v1/payment/execute/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void scenario9() throws Exception {
        mockMvc.perform(put("/api/v1/payment/cancel/nonexistent"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void scenario10() throws Exception {
        generator.generate();

        assertStatistics(RandomUtils.nextInt(1, 4));
        assertStatistics(RandomUtils.nextInt(4, 11));
    }

    private void assertStatistics(int periodInSeconds) throws Exception {
        Statistics expected = generator.calculate(periodInSeconds);

        final String actual = mockMvc.perform(get("/api/v1/statistics/" + periodInSeconds))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        final StringWriter writer = new StringWriter();
        mapper.writeValue(writer, expected);
        assertEquals(writer.toString(), actual);
    }

    private String createAccount(Double balance) throws Exception {
        return mapper.readTree(mockMvc.perform(post("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(new CreateAccount(balance))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString()).findValue("id").asText();
    }
}
