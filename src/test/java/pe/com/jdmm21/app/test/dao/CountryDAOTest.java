package pe.com.jdmm21.app.test.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import pe.com.jdmm21.app.dao.CountryDAOImpl;
import pe.com.jdmm21.app.model.Country;
import pe.com.jdmm21.app.test.config.TestDBConfiguration;

@RunWith(SpringRunner.class)
@SpringJUnitConfig( classes = {
      TestDBConfiguration.class, CountryDAOImpl.class})
public class CountryDAOTest {

  @Autowired CountryDAOImpl countryDao;

  @Autowired @Qualifier("testTemplate")
  NamedParameterJdbcTemplate namedParamJdbcTemplate;

  @Before
  public void setup() {
    countryDao.setNamedParamJdbcTemplate(namedParamJdbcTemplate);
  }

  @Test
  public void testGetCountries() {
    List<Country> countries = countryDao.getCountries(new HashMap());
    //AssertJ assertions
    //Paginated List, so should have 20 entries
    assertThat(countries).hasSize(10);
  }

  @Test
  public void testGetCountries_searchByName() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("search", "Aruba");
    List<Country> countries = countryDao.getCountries(params);
    assertThat(countries).hasSize(1);
  }


  @Test
  public void testGetCountries_searchByContinent() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("continent", "Asia");
    List<Country> countries = countryDao.getCountries(params);

    assertThat(countries).hasSize(10);
  }

  @Test
  public void testGetCountryDetail() {
    Country c = countryDao.getCountryDetail("IND");
    assertThat(c).isNotNull();
    assertThat(c.toString()).isEqualTo("Country(code=IND, name=India, "
        + "continent=Asia, region=Southern and Central Asia, "
        + "surfaceArea=3287263.0, indepYear=1947, population=1013662000, "
        + "lifeExpectancy=62.5, gnp=447114.0, localName=Bharat/India, "
        + "governmentForm=Federal Republic, headOfState=Kocheril Raman Narayanan, "
        + "capital=City(id=1109, name=New Delhi, countryCode=null, "
        + "country=null, district=null, population=null), code2=IN)");
  }

  @Test public void testEditCountryDetail() {
    Country c = countryDao.getCountryDetail("IND");
    c.setHeadOfState("Ram Nath Kovind");
    c.setPopulation(1324171354l);
    countryDao.editCountryDetail("IND", c);

    c = countryDao.getCountryDetail("IND");
    assertThat(c.getHeadOfState()).isEqualTo("Ram Nath Kovind");
    assertThat(c.getPopulation()).isEqualTo(1324171354l);
  }

  @Test public void testGetCountriesCount() {
    Integer count = countryDao.getCountriesCount(Collections.EMPTY_MAP);
    assertThat(count).isEqualTo(239);
  }
}