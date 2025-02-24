package com.maymar.rewards.program.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.maymar.rewards.program.dto.RewardsResponseDto;
import com.maymar.rewards.program.dto.Transaction;
import com.maymar.rewards.program.service.RewardsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardsController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RewardsService rewardsService;

    RewardsResponseDto rewardsResponse;

    @BeforeEach
    void setup(){
        Map<String, Integer> expectedData = new HashMap<>();
        expectedData.put("JANUARY-2025", 1650);
        expectedData.put("FEBRUARY-2025", 210);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, new BigDecimal("100.0"), LocalDate.parse("2025-01-22")));
        transactions.add(new Transaction(12, new BigDecimal("500.0"), LocalDate.parse("2025-01-19")));
        transactions.add(new Transaction(19, new BigDecimal("450.0"), LocalDate.parse("2025-01-30")));
        transactions.add(new Transaction(5, new BigDecimal("60.0"), LocalDate.parse("2025-02-04")));
        transactions.add(new Transaction(11, new BigDecimal("175.0"), LocalDate.parse("2025-02-20")));

        rewardsResponse = new RewardsResponseDto("Mayuresh",
                expectedData,
                1660,
                transactions,
                LocalDate.now());
    }

    @Test
    void getRewardsForSelectedPeriodTest_Should_ReturnFailure1000_InvalidUserIdFormat() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/calculateRewards").
                queryParam("userId", "  ").
                queryParam("period", "LIFETIME"))
        .andExpect(status().is4xxClientError())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

		String timeStamp = JsonPath.read(result.getResponse().getContentAsString(), "$.timeStamp");
		String statusCode = JsonPath.read(result.getResponse().getContentAsString(), "$.statusCode");
		String message = JsonPath.read(result.getResponse().getContentAsString(), "$.message");
		String details = JsonPath.read(result.getResponse().getContentAsString(), "$.details");

		Assertions.assertNotNull(timeStamp);
		Assertions.assertEquals("1000", statusCode);
		Assertions.assertEquals("Please enter request parameters in valid format", message);
		Assertions.assertEquals("Please enter a valid userId. Non empty and less than 20 chars..", details);
    }

    @Test
	void getRewardsForSelectedPeriodTest_Should_ReturnFailure1000_InvalidDateFormat() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/calculateRewards").
						queryParam("userId", "Mayuresh").
                        queryParam("period", "CUSTOMIZE").
                        queryParam("startDate", " ").
                        queryParam("endDate", " "))
				.andExpect(status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String timeStamp = JsonPath.read(result.getResponse().getContentAsString(), "$.timeStamp");
		String statusCode = JsonPath.read(result.getResponse().getContentAsString(), "$.statusCode");
		String message = JsonPath.read(result.getResponse().getContentAsString(), "$.message");
		String details = JsonPath.read(result.getResponse().getContentAsString(), "$.details");

		Assertions.assertNotNull(timeStamp);
		Assertions.assertEquals("1000", statusCode);
		Assertions.assertEquals("Please enter request parameters in valid format", message);
		Assertions.assertEquals("Please enter a valid date. Non empty and in 'YYYY-MM-DD' format", details);
	}

    @Test
    void getRewardsForSelectedPeriodTest_Should_ReturnFailure1001_InvalidDateFormat() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/calculateRewards").
                        queryParam("userId", "Mayuresh").
                        queryParam("period", "CUSTOMIZE").
                        queryParam("startDate", "2025-01-01").
                        queryParam("endDate", "2025-02-30"))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String timeStamp = JsonPath.read(result.getResponse().getContentAsString(), "$.timeStamp");
        String statusCode = JsonPath.read(result.getResponse().getContentAsString(), "$.statusCode");
        String message = JsonPath.read(result.getResponse().getContentAsString(), "$.message");
        String details = JsonPath.read(result.getResponse().getContentAsString(), "$.details");

        Assertions.assertNotNull(timeStamp);
        Assertions.assertEquals("1001", statusCode);
        Assertions.assertEquals("Please enter date in a valid format...", message);
        Assertions.assertNotNull(details);
    }

    @Test
    void getRewardsForSelectedPeriodTest_Should_ReturnFailure1003() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/calculateRewards").
                        queryParam("userId", "Mayuresh").
                        queryParam("period", "LASTMONTH")
                )
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String timeStamp = JsonPath.read(result.getResponse().getContentAsString(), "$.timeStamp");
        String statusCode = JsonPath.read(result.getResponse().getContentAsString(), "$.statusCode");
        String message = JsonPath.read(result.getResponse().getContentAsString(), "$.message");
        String details = JsonPath.read(result.getResponse().getContentAsString(), "$.details");

        Assertions.assertNotNull(timeStamp);
        Assertions.assertEquals("1003", statusCode);
        Assertions.assertEquals("Invalid request parameter value...", message);
        Assertions.assertEquals("Value of 'period' parameter should be 'LIFETIME', 'LASTTHREEMONTHS' or 'CUSTOMIZE'", details);
    }

    @Test
    void getRewardsForSelectedPeriodTest_Should_ReturnSuccessJSON_LIFETIME () throws Exception{
        given(rewardsService.calculateLifetimeRewards(anyString())).willReturn(rewardsResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/calculateRewards").
                        queryParam("userId", "Mayuresh").
                        queryParam("period", "LIFETIME"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONAssert.assertEquals(result.getResponse().getContentAsString(),
                objectMapper.writeValueAsString(rewardsResponse),
                false);
    }
}
