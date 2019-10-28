package pe.com.jdmm21.app.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pe.com.jdmm21.app.dao.CountryDAOImpl;
import pe.com.jdmm21.app.external.WorldBankApiClient;
import pe.com.jdmm21.app.model.Country;

@RestController
@RequestMapping("/api/countries")
@Slf4j
public class CountryAPIController {
	
	@Autowired
	CountryDAOImpl countryDAOImpl;
	
	@Autowired
	WorldBankApiClient worldBankApiClient;
	
	@GetMapping
	public ResponseEntity<?> getCountries(
			@RequestParam(name = "search",required = false) String searchTerm,
			@RequestParam(name = "continent",required = false) String continent,
			@RequestParam(name = "region",required = false) String region,
			@RequestParam(name = "pageNo",required = false) Integer pageNo
			){
		
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("search", searchTerm);
			params.put("continent", continent);
			params.put("region", region);
			if(pageNo!=null) {
				params.put("pageNo", pageNo.toString());
			}
			
			List<Country> countries = countryDAOImpl.getCountries(params);
			Map<String, Object> response = new HashMap<String, Object>();
			response.put("list", countries);
			response.put("count", countryDAOImpl.getCountriesCount(params));
			return ResponseEntity.ok(response);
 		}catch (Exception e) {
 			System.out.println("Error while getting countries"+ e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while getting countries");
 		}
	}

	@PostMapping(value = "/{countryCode}", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> editCountry(
			@PathVariable String countryCode, 
			@Valid @RequestBody Country country
			){
		try {
			countryDAOImpl.editCountryDetail(countryCode, country);
			Country countryFromDb = countryDAOImpl.getCountryDetail(countryCode);
			return ResponseEntity.ok(countryFromDb);
		} catch (Exception e) {
			System.out.println("Error while editing the country {} with data {} " + countryCode + country + e);
			return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{countryCode}/gdp")
	public ResponseEntity<?> getGDP(
			@PathVariable String countryCode
			) {
		try {
			return ResponseEntity.ok(worldBankApiClient.getGDP(countryCode));
		} catch (Exception e) {
			log.info("Error while getting GDP for country: {}", countryCode);
			log.info("Exception: {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while getting GDP");
		}
	
	}
}
