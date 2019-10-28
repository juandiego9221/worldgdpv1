package pe.com.jdmm21.app.dao;

import java.util.List;
import java.util.Map;

import pe.com.jdmm21.app.model.City;

public interface CityDAO {
	
	public List<City> getCities(String countryCode);
	public List<City> getCities(String countryCode,Integer pageNo);
	public City getCityDetail(Long cityId);
	public Long addCity(String countryCode, City city );
	public void deleteCity(Long cityid);
	public Map<String, Object> getMapForCity(String countryCode,City city);
	
	

}
