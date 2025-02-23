package com.maymar.rewards.program;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.maymar.rewards.program.dto.RewardsResponseDto;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class RewardsProgramApplicationIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void getLifetimeRewards_Should_ReturnSuccessJson() throws Exception{

		Map<String, Integer> expectedData = new HashMap<>();
		expectedData.put("JANUARY-2025", 1650);
		expectedData.put("FEBRUARY-2025", 210);
		expectedData.put("MARCH-2025", 13);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/lifetime").
						queryParam("userId", "Mayuresh"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		JSONAssert.assertEquals(result.getResponse().getContentAsString(),
				objectMapper.writeValueAsString(new RewardsResponseDto("Mayuresh", expectedData, 1873, LocalDate.now())),
				false);
	}

	@Test
	void getLifetimeRewards_Should_ReturnFailureStatus_1000() throws Exception{

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/lifetime").
						queryParam("userId", "   "))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String timeStamp = JsonPath.read(result.getResponse().getContentAsString(), "$.timeStamp");
		String statusCode = JsonPath.read(result.getResponse().getContentAsString(), "$.statusCode");
		String message = JsonPath.read(result.getResponse().getContentAsString(), "$.message");
		String details = JsonPath.read(result.getResponse().getContentAsString(), "$.details");

		Assertions.assertNotNull(timeStamp);
		Assertions.assertEquals("1000", statusCode);
		Assertions.assertEquals("Please enter userId in a valid format...", message);
		Assertions.assertNotNull(details);
	}

	@Test
	void getLifetimeRewards_Should_ReturnFailureStatus_1002() throws Exception{

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/lifetime").
						queryParam("userId", "Test"))
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
	void getLastThreeMonthRewards_Should_ReturnSuccessJson() throws Exception{

		Map<String, Integer> expectedData = new HashMap<>();
		expectedData.put("JANUARY-2025", 1650);
		expectedData.put("FEBRUARY-2025", 210);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/lastThreeMonths").
						queryParam("userId", "Mayuresh"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		JSONAssert.assertEquals(result.getResponse().getContentAsString(),
				objectMapper.writeValueAsString(new RewardsResponseDto("Mayuresh", expectedData, 1860, LocalDate.now())),
				false);
	}

	@Test
	void getRewardsForGivenPeriod_Should_ReturnSuccessJson() throws Exception {

		Map<String, Integer> expectedData = new HashMap<>();
		expectedData.put("FEBRUARY-2025", 210);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/specifiedPeriod").
						queryParam("userId", "Mayuresh").
						queryParam("startDate", "2025-02-01").
						queryParam("endDate", "2025-02-28")
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		JSONAssert.assertEquals(result.getResponse().getContentAsString(),
				objectMapper.writeValueAsString(new RewardsResponseDto("Mayuresh", expectedData, 210, LocalDate.now())),
				false);
	}

	@Test
	void getRewardsForGivenPeriod_Should_ReturnFailureStatus_1000() throws Exception {


		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/specifiedPeriod").
						queryParam("userId", "  ").
						queryParam("startDate", "2025-02-01").
						queryParam("endDate", "2025-02-28")
				)
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String timeStamp = JsonPath.read(result.getResponse().getContentAsString(), "$.timeStamp");
		String statusCode = JsonPath.read(result.getResponse().getContentAsString(), "$.statusCode");
		String message = JsonPath.read(result.getResponse().getContentAsString(), "$.message");
		String details = JsonPath.read(result.getResponse().getContentAsString(), "$.details");

		Assertions.assertNotNull(timeStamp);
		Assertions.assertEquals("1000", statusCode);
		Assertions.assertEquals("Please enter userId in a valid format...", message);
		Assertions.assertNotNull(details);
	}

	@Test
	void getRewardsForGivenPeriod_Should_ReturnFailureStatus_1001() throws Exception {


		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rewards/api/specifiedPeriod").
						queryParam("userId", "Mayuresh").
						queryParam("startDate", "2025-02-01").
						queryParam("endDate", "2025-02-30")
				)
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
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
}
