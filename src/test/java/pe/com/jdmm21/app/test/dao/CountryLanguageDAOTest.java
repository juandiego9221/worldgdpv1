package pe.com.jdmm21.app.test.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import pe.com.jdmm21.app.dao.CountryLanguageDAOImpl;
import pe.com.jdmm21.app.model.CountryLanguage;
import pe.com.jdmm21.app.test.config.TestDBConfiguration;

@RunWith(SpringRunner.class)
@SpringJUnitConfig(classes = {TestDBConfiguration.class,CountryLanguageDAOImpl.class})
public class CountryLanguageDAOTest {
	
	@Autowired
	CountryLanguageDAOImpl countryLanguageDAOImpl;
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Before
	public void before() {
		countryLanguageDAOImpl.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
	}

	@Test
	public void testGetLanguage() {
		List<CountryLanguage> languages = countryLanguageDAOImpl.getLanguages("IND", 1);
		assertThat(languages).hasSize(10);
	}
	
//	@Test
//	public void testAddLanguages(){
//		String countryCode ="IND";
//		CountryLanguage cl = createLanguage(countryCode);
//		countryLanguageDAOImpl.addLanguage(countryCode, cl);
//		List<CountryLanguage> languages = countryLanguageDAOImpl.getLanguages(countryCode, 2);
//		assertThat(languages).hasSize(3);
//	}
	
	@Test
	public void testLanguageExists() {
		String countryCode = "IND";
		CountryLanguage cl= createLanguage(countryCode);
		countryLanguageDAOImpl.addLanguage(countryCode, cl);
		assertThat(countryLanguageDAOImpl.languageExists(countryCode, cl.getLanguage())).isTrue();
		countryLanguageDAOImpl.deleteLanguage(countryCode, cl.getLanguage());
	}
	
	@Test
	public void testDeleteLanguage() {
		String countryCode = "IND";
		CountryLanguage cl = createLanguage(countryCode);
		countryLanguageDAOImpl.addLanguage(countryCode, cl);
		List<CountryLanguage> languages = countryLanguageDAOImpl.getLanguages(countryCode, 2);
		assertThat(languages).hasSize(3);
		
		countryLanguageDAOImpl.deleteLanguage(countryCode, "Test");
		languages = countryLanguageDAOImpl.getLanguages(countryCode, 2);
		assertThat(languages).hasSize(2);
	}
	
	private CountryLanguage createLanguage(String countryCode) {
		CountryLanguage cl = new CountryLanguage();
		cl.setCountryCode(countryCode);
		cl.setIsOfficial("T");
		cl.setLanguage("Test");
		cl.setPercentage(12.3);
		return cl;
	}
	
	
	

}
