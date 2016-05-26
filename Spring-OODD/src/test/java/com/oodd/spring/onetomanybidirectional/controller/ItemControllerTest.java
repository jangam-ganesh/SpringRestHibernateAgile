package com.oodd.spring.onetomanybidirectional.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.oodd.spring.onetomanybidirectional.dto.ItemDto;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration( locations = { "classpath:context.xml" } )
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ItemControllerTest {
	private final Gson gson = new GsonBuilder().create();

	@Resource
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.<StandaloneMockMvcBuilder>webAppContextSetup(webApplicationContext).build();
	}
	@Test
	public void testAll() throws Exception {
		testCreate();
		testUpdate();
		testDelete();
	}
	public void testCreate() throws Exception {
		ItemDto itemDto = new ItemDto();
		itemDto.setName("Book");
		List<String> featurelist = new ArrayList<String>();
		featurelist.add("Java");
		featurelist.add("J2ee");
		itemDto.setFeatureList(featurelist);
		String json = gson.toJson(itemDto);
		MockHttpServletRequestBuilder requestBuilderOne = MockMvcRequestBuilders.post("/onetomanybidirectional/create");
		requestBuilderOne.contentType(MediaType.APPLICATION_JSON);
		requestBuilderOne.content(json.getBytes());
		this.mockMvc.perform(requestBuilderOne).andExpect(MockMvcResultMatchers.status().isOk());
	}
	public void testUpdate() throws Exception {
		MockHttpServletRequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/onetomanybidirectional/findAll");
		MvcResult result = this.mockMvc.perform(requestBuilder2).andReturn();
		String response2 = result.getResponse().getContentAsString();
		Type listType = new TypeToken<List<ItemDto>>() {}.getType();
		List<ItemDto> itemDtos = gson.fromJson(response2, listType);
		ItemDto itemDto = itemDtos.get(0);
		List<String> featurelist = new ArrayList<String>();
		featurelist.add("Spring 3.0");
		featurelist.add("Hibernate 4.1");
		itemDto.setFeatureList(featurelist);
		String json2 = gson.toJson(itemDto);
		MockHttpServletRequestBuilder requestBuilder3 = MockMvcRequestBuilders.post("/onetomanybidirectional/edit");
		requestBuilder3.contentType(MediaType.APPLICATION_JSON);
		requestBuilder3.content(json2.getBytes());
		this.mockMvc.perform(requestBuilder3).andExpect(MockMvcResultMatchers.status().isOk());
	}
	public void testDelete() throws Exception {
		MockHttpServletRequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/onetomanybidirectional/findAll");
		MvcResult result = this.mockMvc.perform(requestBuilder2).andReturn();
		String response2 = result.getResponse().getContentAsString();
		Type listType = new TypeToken<List<ItemDto>>() {}.getType();
		List<ItemDto> itemDtos = gson.fromJson(response2, listType);
		ItemDto itemDto = itemDtos.get(0);
		MockHttpServletRequestBuilder requestBuilder3 = MockMvcRequestBuilders.post("/onetomanybidirectional/remove/"+itemDto.getId());
		requestBuilder3.contentType(MediaType.APPLICATION_JSON);
		this.mockMvc.perform(requestBuilder3).andExpect(MockMvcResultMatchers.status().is(204));
	}
}

