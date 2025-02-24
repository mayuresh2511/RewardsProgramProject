package com.maymar.rewards.program;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.maymar.rewards.program.dto.RewardsResponseDto;
import com.maymar.rewards.program.dto.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

@SpringBootTest
@AutoConfigureMockMvc
public class RewardsProgramApplicationIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void getRewardsForSelectedPeriod_Should_ReturnFailureStatus_1002() throws Exception{

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/calculateRewards").
						queryParam("userId", "TEST").
						queryParam("period", "LIFETIME"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String timeStamp = JsonPath.read(result.getResponse().getContentAsString(), "$.timeStamp");
		String statusCode = JsonPath.read(result.getResponse().getContentAsString(), "$.statusCode");
		String message = JsonPath.read(result.getResponse().getContentAsString(), "$.message");
		String details = JsonPath.read(result.getResponse().getContentAsString(), "$.details");

		Assertions.assertNotNull(timeStamp);
		Assertions.assertEquals("1002", statusCode);
		Assertions.assertEquals("It seems there are no transactions for you....", message);
		Assertions.assertNotNull(details);
	}

	@Test
	void getRewardsForSelectedPeriod_Should_ReturnSuccessJson_LASTTHREEMONTHS() throws Exception{

		Map<String, Integer> expectedData = new HashMap<>();
		expectedData.put("JANUARY-2025", 1650);
		expectedData.put("FEBRUARY-2025", 210);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(new Transaction(1, new BigDecimal("100.0"), LocalDate.parse("2025-01-22")));
		transactions.add(new Transaction(12, new BigDecimal("500.0"), LocalDate.parse("2025-01-19")));
		transactions.add(new Transaction(19, new BigDecimal("450.0"), LocalDate.parse("2025-01-30")));
		transactions.add(new Transaction(5, new BigDecimal("60.0"), LocalDate.parse("2025-02-04")));
		transactions.add(new Transaction(11, new BigDecimal("175.0"), LocalDate.parse("2025-02-20")));

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/calculateRewards").
						queryParam("userId", "Mayuresh").
						queryParam("period", "LASTTHREEMONTHS"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		JSONAssert.assertEquals(result.getResponse().getContentAsString(),
				objectMapper.writeValueAsString(new RewardsResponseDto("Mayuresh",
						expectedData,
						1860,
						transactions,
						LocalDate.now())
				),
				false);
	}

	@Test
	void getRewardsForSelectedPeriod_Should_ReturnSuccessJson_CUSTOMIZE() throws Exception{

		Map<String, Integer> expectedData = new HashMap<>();
		expectedData.put("JANUARY-2025", 1650);
		expectedData.put("FEBRUARY-2025", 10);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(new Transaction(1, new BigDecimal("100.0"), LocalDate.parse("2025-01-22")));
		transactions.add(new Transaction(12, new BigDecimal("500.0"), LocalDate.parse("2025-01-19")));
		transactions.add(new Transaction(19, new BigDecimal("450.0"), LocalDate.parse("2025-01-30")));
		transactions.add(new Transaction(5, new BigDecimal("60.0"), LocalDate.parse("2025-02-04")));

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/calculateRewards").
						queryParam("userId", "Mayuresh").
						queryParam("period", "CUSTOMIZE").
						queryParam("startDate", "2025-01-01").
						queryParam("endDate", "2025-02-15")
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		JSONAssert.assertEquals(result.getResponse().getContentAsString(),
				objectMapper.writeValueAsString(new RewardsResponseDto("Mayuresh",
						expectedData,
						1660,
						transactions,
						LocalDate.now())
				),
				false);
	}
}
