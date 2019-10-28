package pe.com.jdmm21.app.controller.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pe.com.jdmm21.app.dao.CityDAOImpl;
import pe.com.jdmm21.app.model.City;

@Slf4j
@RestController
@RequestMapping("/api/cities")
public class CityAPIController {
	
	@Autowired CityDAOImpl cityDAOImpl;
	
	@GetMapping("/{countryCode}")
	public ResponseEntity<?> getCities(
			@PathVariable String countryCode,
			@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo
			){
		try {
			return new ResponseEntity<>(cityDAOImpl.getCities(countryCode,pageNo),HttpStatus.OK);
		} catch (Exception e) {
			log.info("Error while getting citites for country: {}",countryCode);
			return new ResponseEntity<>("Error while getting citites",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("{countryCode}")
	public ResponseEntity<?> addCity(
			@PathVariable String countryCode,
			@Valid @RequestBody City city
			){
		try {
			cityDAOImpl.addCity(countryCode, city);
			return new ResponseEntity<>(city,HttpStatus.CREATED);
		} catch (Exception e) {
			log.info("Erro while adding city {}  to country {}",city,countryCode);
			return new ResponseEntity<> ("Error while adding city", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{cityId}")
	public ResponseEntity<?> deleteCity(
			@PathVariable Long cityId
			){
		try {
			cityDAOImpl.deleteCity(cityId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.info("Erro while  deleting city {}",cityId);
			return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
