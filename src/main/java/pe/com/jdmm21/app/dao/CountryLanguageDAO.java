package pe.com.jdmm21.app.dao;

import java.util.List;

import pe.com.jdmm21.app.model.CountryLanguage;

public interface CountryLanguageDAO {
	
	public List<CountryLanguage> getLanguages(String countryCode,Integer pageNo);
	public void addLanguage(String countryCode,CountryLanguage cl);
	public boolean languageExists(String countryCode,String lenguage);
	public void deleteLanguage (String countryCode, String lenguage);
	
	
}
