package pe.com.jdmm21.app.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import pe.com.jdmm21.app.model.CountryLanguage;
import pe.com.jdmm21.app.model.mapper.CountryLanguageRowMapper;

@Repository
public class CountryLanguageDAOImpl implements CountryLanguageDAO{

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	private static final Integer PAGE_SIZE = 10;
	
	public List<CountryLanguage> getLanguages(String countryCode, Integer pageNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", countryCode);
		
		Integer offset =( pageNo-1)*PAGE_SIZE;
		params.put("offset", offset);
		params.put("size", PAGE_SIZE);
		
		return namedParameterJdbcTemplate.query(
				"SELECT * FROM countrylanguage"
						+ " WHERE countrycode = :code"
						+ " ORDER BY percentage DESC "
						+ " LIMIT :size OFFSET :offset "
				, params, new CountryLanguageRowMapper());
	}

	public void addLanguage(String countryCode, CountryLanguage cl) {
		namedParameterJdbcTemplate.update(
				"INSERT INTO countrylanguage ( "
						+ " countrycode, language, isofficial, percentage ) "
						+ " VALUES ( :country_code, :language, "
						+ " :is_official, :percentage ) "
				, getAsMap(countryCode, cl));
	}

	public boolean languageExists(String countryCode, String lenguage) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", countryCode);
		params.put("lang", lenguage);
		
		Integer langCount = namedParameterJdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM countrylanguage"
						+ " WHERE countrycode = :code "
						+ " AND language = :lang"
				, params, Integer.class);
		
		return langCount>0;
	}

	public void deleteLanguage(String countryCode, String lenguage) {
		Map<String, Object> params = new HashMap<String , Object>();
		params.put("code", countryCode);
		params.put("lang", lenguage);
		namedParameterJdbcTemplate.update(
				"DELETE FROM countrylanguage "
						+ " WHERE countrycode = :code AND "
						+ " language = :lang "
				, params);
	}
	
	private Map<String , Object> getAsMap(String countryCode, CountryLanguage cl){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("country_code", countryCode);
		map.put("language", cl.getLanguage());
		map.put("is_official", cl.getIsOfficial());
		map.put("percentage", cl.getPercentage());
		return map;
	}

}
