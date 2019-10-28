package pe.com.jdmm21.app.dao;

import java.util.List;
import java.util.Map;

import pe.com.jdmm21.app.model.Country;

public interface CountryDAO {
	
	public List<Country> getCountries(Map<String, Object> params);
	public int getCountriesCount (Map<String, Object> params);
	public Country getCountryDetail(String code);
	public void editCountryDetail(String code, Country country);
	public Map<String, Object> getCountryAsMap(String code, Country country);
}

