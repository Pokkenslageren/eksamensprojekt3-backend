package org.example.eksamensprojekt3sem.Member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.eksamensprojekt3sem.Enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
public class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Member createValidMember(String name, PaymentStatus status) throws Exception {
        Member member = new Member();
        member.setName(name);
        member.setEmail("hanne@test.dk");
        member.setPhone("12345678");
        member.setAddress("Testvej 1");
        member.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
        member.setPaymentStatus(status);
        return member;
    }

    @Test
    void createAndRetrieveMember() throws Exception {
        Member member = createValidMember("Frederik Testsen", PaymentStatus.UNPAID);

        MvcResult result = mockMvc.perform(post("/fodboldklub/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isOk())
                .andReturn();

        Member created = objectMapper.readValue(result.getResponse().getContentAsString(), Member.class);

        mockMvc.perform(get("/fodboldklub/members/" + created.getMemberId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Frederik Testsen"))
                .andDo(print())
                .andExpect(jsonPath("$.paymentStatus").value("UNPAID"));
    }

    @Test
    void updatePaymentStatus() throws Exception {
        Member member = createValidMember("Kasper Betalt", PaymentStatus.UNPAID);

        MvcResult result = mockMvc.perform(post("/fodboldklub/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isOk())
                .andReturn();

        Member created = objectMapper.readValue(result.getResponse().getContentAsString(), Member.class);

        PaymentStatusDTO dto = new PaymentStatusDTO();
        dto.setStatus("PAID");

        mockMvc.perform(put("/fodboldklub/members/" + created.getMemberId() + "/payment-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentStatus").value("PAID"));
    }

    @Test
    void getMembersByPaymentStatus() throws Exception {
        Member member = createValidMember("Pia Paid", PaymentStatus.PAID);

        mockMvc.perform(post("/fodboldklub/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/fodboldklub/members/member/search/paymentstatus")
                        .param("paymentstatus", "PAID"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Pia Paid"));
    }
}
