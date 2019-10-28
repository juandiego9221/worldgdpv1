package pe.com.jdmm21.app.controller.view;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pe.com.jdmm21.app.dao.CityDAOImpl;
import pe.com.jdmm21.app.dao.CountryDAOImpl;
import pe.com.jdmm21.app.dao.LookupDAOImpl;

@Controller
@RequestMapping("/")
public class ViewController {

	@Autowired
	CountryDAOImpl countryDAOImpl;
	@Autowired
	CityDAOImpl cityDAOImpl;
	@Autowired
	LookupDAOImpl lookupDAOImpl;

	@GetMapping({ "/countries", "/" })
	public String countries(Model model, @RequestParam Map<String, Object> params) {
		model.addAttribute("continent", lookupDAOImpl.getContinents());
		model.addAttribute("regions", lookupDAOImpl.getRegions());
		model.addAttribute("countries", countryDAOImpl.getCountries(params));
		model.addAttribute("count", countryDAOImpl.getCountriesCount(params));
		return "countries";
	}

	@GetMapping("/countries/{code}")
	public String countryDetail(Model model, @PathVariable String code) {
		model.addAttribute("c", countryDAOImpl.getCountryDetail(code));
		return "country";
	}

	@GetMapping("/countries/{code}/form")
	public String editCountry(Model model, @PathVariable String code) {
		model.addAttribute("c", countryDAOImpl.getCountryDetail(code));
		model.addAttribute("cities", cityDAOImpl.getCities(code));
		model.addAttribute("continents", lookupDAOImpl.getContinents());
		model.addAttribute("regions", lookupDAOImpl.getRegions());
		model.addAttribute("heads", lookupDAOImpl.getHeadOfStates());
		model.addAttribute("govs", lookupDAOImpl.getGovernmentTypes());
		return "country-form";
	}

}
