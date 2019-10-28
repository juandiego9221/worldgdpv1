package pe.com.jdmm21.app.external;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pe.com.jdmm21.app.model.CountryGDP;

@Service
public class WorldBankApiClient {
	String GDP_URL = "http://api.worldbank.org/countries/%s/indicators/NY.GDP.MKTP.CD?"
			+ "format=json&date=2008:2018";
	
	public List<CountryGDP> getGDP(String countryCode) throws ParseException{
		RestTemplate worldBankRestTemplate = new RestTemplate();
		ResponseEntity<String> response = worldBankRestTemplate.getForEntity(
				String.format(GDP_URL, countryCode)
				, String.class);
		
		JSONParser parser = new JSONParser();
		JSONArray responseData = (JSONArray) parser.parse(response.getBody());
		JSONArray countryDataArr = (JSONArray) responseData.get(1);
		
		List<CountryGDP> data = new ArrayList<>();
		JSONObject countryDataYearWise = null;
		
		for (int index = 0; index < countryDataArr.size(); index++) {
			countryDataYearWise = (JSONObject) countryDataArr.get(index);
			
			String valueStr = "0";
			if(countryDataYearWise.get("value")!=null) {
				valueStr = (String) countryDataYearWise.get("value").toString();
			}
			String yearStr = (String) countryDataYearWise.get("date").toString();
			CountryGDP gdp = new CountryGDP();
			gdp.setValue(valueStr!=null?Double.valueOf(valueStr):null);
			gdp.setYear(Short.valueOf(yearStr));
			data.add(gdp);
		}
		
		return data;
	}

}
