package pe.com.jdmm21.app.test.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import pe.com.jdmm21.app.dao.CityDAOImpl;
import pe.com.jdmm21.app.model.City;
import pe.com.jdmm21.app.test.config.TestDBConfiguration;

@RunWith(SpringRunner.class)
@SpringJUnitConfig(
		classes = {TestDBConfiguration.class,CityDAOImpl.class}
		)
public class CityDAOTest {
	
	@Autowired
	CityDAOImpl cityDao;
	
	@Autowired
	@Qualifier("testTemplate")
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Before
	public void setup() {
		cityDao.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
	}
	
	@Test
	public void testGetCities() {
		List<City> cities = cityDao.getCities("IND", 1);
		assertThat(cities).hasSize(10);
	}
	
	@Test
	public void testGetCityDetail() {
		Long cityId = 1024l;
		City city  = cityDao.getCityDetail(cityId);
		assertThat(city.toString()).isEqualTo(
				"City(id=1024, name=Mumbai (Bombay), "
					      + "countryCode=IND, country=null, district=Maharashtra, population=10500000)"
				);
	}
	
	@Test
	public void testAddCity() {
	    String countryCode = "IND";
	    City city = new City();
	    city.setCountryCode(countryCode);
	    city.setDistrict("District");
	    city.setName("City Name");
	    city.setPopulation(101010l);
	    
	    long cityId = cityDao.addCity(countryCode, city);
	    assertThat(cityId).isNotNull();
	    City cityFromDb = cityDao.getCityDetail(cityId);
	    assertThat(cityFromDb).isNotNull();
	    assertThat(cityFromDb.getName()).isEqualTo("City Name");
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testDeleteCity() {
		Long cityId = addCity();
		cityDao.deleteCity(cityId);
		City cityFromDb = cityDao.getCityDetail(cityId);
		assertThat(cityFromDb).isNotNull();
	}
	
	public Long addCity() {
		String countryCode = "IND";
		City city = new City();
		city.setCountryCode(countryCode);
		city.setDistrict("District");
		city.setName("City Name");
		city.setPopulation(101010l);
		
		return cityDao.addCity(countryCode, city);
	}
	
	
	

}
