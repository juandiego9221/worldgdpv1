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

import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.jdmm21.app.AppConfiguration;
import pe.com.jdmm21.app.dao.CityDAOImpl;
import pe.com.jdmm21.app.model.City;

@RunWith(SpringRunner.class)
@SpringJUnitWebConfig(classes = { AppConfiguration.class })
public class CityAPIControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	@Autowired
	CityDAOImpl cityDAOImpl;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		cityDAOImpl.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
	}

	@Test
	public void testGetCities() throws Exception {
		String countryCode = "IND";
		this.mockMvc
				.perform(get("/api/cities/{countryCode}", countryCode)
						.accept(MediaType.parseMediaType("application/json;charset=UTF8")))
				.andExpect(status().isOk())
				// .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()", is(10)))
				.andExpect(jsonPath("$[0].name", is("Mumbai (Bombay)")));
	}

	@Test
	public void testAddCity() throws Exception {
		String countryCode = "IND";
		City city = new City();
		city.setCountryCode(countryCode);
		city.setDistrict("Karnataka");
		city.setName("Large State");
		city.setPopulation(10500000L + 100);

		ObjectMapper objectMapper = new ObjectMapper();
		MvcResult result = this.mockMvc.perform(post("/api/cities/{countryCode}", countryCode)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(city)))
				.andExpect(status().isCreated()).andReturn();

		List<City> cities = cityDAOImpl.getCities(countryCode, 1);
		City first = cities.get(0);
		assertThat(first.getName()).isEqualTo("Large State");
		cityDAOImpl.deleteCity(first.getId());
	}

	@Test
	public void testDeleteCity() throws Exception {
		String countryCode = "IND";
		City city = new City();
		city.setCountryCode(countryCode);
		city.setDistrict("Karnataka");
		city.setName("Large State");
		city.setPopulation(10500000L + 100);
		
		Long cityId = cityDAOImpl.addCity(	countryCode, city);
		this.mockMvc.perform(delete("/api/cities/{cityId}",cityId))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk());
		
		List<City> cities = cityDAOImpl.getCities(countryCode,1);
		City first = cities.get(0);
		assertThat(first.getName()).isEqualTo("Mumbai (Bombay)");
		
	}

}
