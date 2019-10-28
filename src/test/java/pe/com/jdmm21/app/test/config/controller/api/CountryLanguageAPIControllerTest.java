package pe.com.jdmm21.app.test.config.controller.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.jdmm21.app.AppConfiguration;
import pe.com.jdmm21.app.dao.CountryLanguageDAOImpl;
import pe.com.jdmm21.app.model.CountryLanguage;

@RunWith(SpringRunner.class)
@SpringJUnitWebConfig(classes = AppConfiguration.class)
public class CountryLanguageAPIControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Autowired
	CountryLanguageDAOImpl countryLanguageDAOImpl;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Before
	public void setuo() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		countryLanguageDAOImpl.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
	}

	@Test
	public void testGetLanguages() throws Exception {
		String countryCode = "IND";
		this.mockMvc.perform(get("/api/languages/{countryCode}", countryCode).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				//.andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()", is(10)))
				.andExpect(jsonPath("$[0].language", is("Hindi")));
	}

	@Test
	public void testAddLanguage() throws Exception {
		String countryCode = "IND";
		CountryLanguage countryLanguage = new CountryLanguage();
		countryLanguage.setCountryCode(countryCode);
		countryLanguage.setIsOfficial("1");
		countryLanguage.setLanguage("TEST");
		countryLanguage.setPercentage(100d);

		ObjectMapper objectMapper = new ObjectMapper();
		;
		MvcResult result = this.mockMvc
				.perform(post("/api/languages/{countryCode}", countryCode).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(countryLanguage)))
				.andExpect(status().isOk()).andReturn();

		List<CountryLanguage> langs = countryLanguageDAOImpl.getLanguages(countryCode, 1);
		CountryLanguage first = langs.get(0);
		assertThat(first.getLanguage()).isEqualTo("TEST");
		countryLanguageDAOImpl.deleteLanguage(countryCode, first.getLanguage());
	}

	@Test
	public void testAddLanguage_DuplicateLanguage() throws Exception {
		String countryCode = "IND";

		CountryLanguage countryLanguage = new CountryLanguage();
		countryLanguage.setCountryCode(countryCode);
		countryLanguage.setIsOfficial("1");
		countryLanguage.setLanguage("TEST");
		countryLanguage.setPercentage(100d);

		countryLanguageDAOImpl.addLanguage(countryCode, countryLanguage);

		ObjectMapper objectMapper = new ObjectMapper();
		MvcResult result = this.mockMvc
				.perform(post("/api/languages/{countryCode}", countryCode).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(countryLanguage)))
				.andExpect(status().isBadRequest()).andReturn();

		countryLanguageDAOImpl.deleteLanguage(countryCode, countryLanguage.getLanguage());
	}

	@Test
	public void testDeleteCity() throws Exception {
		String countryCode = "IND";
		CountryLanguage countryLanguage = new CountryLanguage();
		countryLanguage.setCountryCode(countryCode);
		countryLanguage.setIsOfficial("1");
		countryLanguage.setLanguage("TEST");
		countryLanguage.setPercentage(100d);

		countryLanguageDAOImpl.addLanguage(countryCode, countryLanguage);
		this.mockMvc.perform(
				delete("/api/languages/{countryCode}/language/{language}", countryCode, countryLanguage.getLanguage()))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());

		List<CountryLanguage> langs = countryLanguageDAOImpl.getLanguages(countryCode, 1);
		CountryLanguage first = langs.get(0);
		assertThat(first.getLanguage()).isEqualTo("Hindi");
	}

}
