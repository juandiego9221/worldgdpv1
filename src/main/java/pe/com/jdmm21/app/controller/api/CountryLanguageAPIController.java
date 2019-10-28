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
import pe.com.jdmm21.app.dao.CountryLanguageDAOImpl;
import pe.com.jdmm21.app.model.CountryLanguage;

@Slf4j
@RestController
@RequestMapping("/api/languages")
public class CountryLanguageAPIController {

	@Autowired
	CountryLanguageDAOImpl countryLanguageDAOImpl;

	@GetMapping("/{countryCode}")
	public ResponseEntity<?> getLanguages(@PathVariable String countryCode,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
		try {
			return ResponseEntity.ok(countryLanguageDAOImpl.getLanguages(countryCode, pageNo));
		} catch (Exception e) {
			log.info("Error while getting languages for country {}", countryCode);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while getting languages");
		}
	}

	@PostMapping("/{countryCode}")
	public ResponseEntity<?> addLanguage(@PathVariable String countryCode,
			@Valid @RequestBody CountryLanguage countryLanguage) {
		try {
			if (countryLanguageDAOImpl.languageExists(countryCode, countryLanguage.getLanguage())) {
				return ResponseEntity.badRequest().body("language already exits in  that country");
			}

			countryLanguageDAOImpl.addLanguage(countryCode, countryLanguage);
			return ResponseEntity.ok(countryLanguage);
		} catch (Exception e) {
			log.info("Erro while adding language {} to country {}", countryLanguage, countryCode);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro while adding language");
		}
	}

	@DeleteMapping("/{countryCode}/language/{language}")
	public ResponseEntity<?> deleteLanguage(@PathVariable String countryCode, @PathVariable String language) {
		try {
			countryLanguageDAOImpl.deleteLanguage(countryCode, language);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.info("Erro while deleting language {} for country {}", language, countryCode);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while deleting language");
		}
	}

}
