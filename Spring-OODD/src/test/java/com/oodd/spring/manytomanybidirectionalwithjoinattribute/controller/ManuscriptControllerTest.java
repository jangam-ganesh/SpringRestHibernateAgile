package com.oodd.spring.manytomanybidirectionalwithjoinattribute.controller;

import java.lang.reflect.Type;
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
import com.oodd.spring.manytomanybidirectionalwithjoinattribute.dto.ManuscriptDto;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration( locations = { "classpath:context.xml" } )
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ManuscriptControllerTest {

	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
	
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
		ManuscriptDto manuscriptDto = new ManuscriptDto();
		manuscriptDto.setName("The Secret of the Nagas");
		
		String json = gson.toJson(manuscriptDto);
		
		MockHttpServletRequestBuilder requestBuilderOne = MockMvcRequestBuilders.post("/manytomanybidirectionalwithjoinattribute/manuscript/create");
		requestBuilderOne.contentType(MediaType.APPLICATION_JSON);
		requestBuilderOne.content(json.getBytes());
		this.mockMvc.perform(requestBuilderOne).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	public void testUpdate() throws Exception {		
		MockHttpServletRequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/manytomanybidirectionalwithjoinattribute/manuscript/findAll");
		MvcResult result = this.mockMvc.perform(requestBuilder2).andReturn();
		String response2 = result.getResponse().getContentAsString();
		Type listType = new TypeToken<List<ManuscriptDto>>() {}.getType();
		
		List<ManuscriptDto> manuscriptDtoList = gson.fromJson(response2, listType);
		ManuscriptDto manuscriptDto2 = manuscriptDtoList.get(0);
		manuscriptDto2.setName("The Immortals of Meluha");
		
		String json2 = gson.toJson(manuscriptDto2);
		MockHttpServletRequestBuilder requestBuilder3 = MockMvcRequestBuilders.post("/manytomanybidirectionalwithjoinattribute/manuscript/edit");
		requestBuilder3.contentType(MediaType.APPLICATION_JSON);
		requestBuilder3.content(json2.getBytes());
		this.mockMvc.perform(requestBuilder3).andExpect(MockMvcResultMatchers.status().isOk());
	}

	public void testDelete() throws Exception {		
		MockHttpServletRequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/manytomanybidirectionalwithjoinattribute/manuscript/findAll");
		MvcResult result = this.mockMvc.perform(requestBuilder2).andReturn();
		String response2 = result.getResponse().getContentAsString();
		Type listType = new TypeToken<List<ManuscriptDto>>() {}.getType();
		
		List<ManuscriptDto> manuscriptDtoList = gson.fromJson(response2, listType);
		ManuscriptDto manuscriptDto2 = manuscriptDtoList.get(0);
		
		MockHttpServletRequestBuilder requestBuilder3 = MockMvcRequestBuilders.post("/manytomanybidirectionalwithjoinattribute/manuscript/remove/"+manuscriptDto2.getId());
		requestBuilder3.contentType(MediaType.APPLICATION_JSON);
		this.mockMvc.perform(requestBuilder3).andExpect(MockMvcResultMatchers.status().is(204));
	}
}