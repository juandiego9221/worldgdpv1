package pe.com.jdmm21.app.dao;

import java.util.List;

public interface LookupDAO {
	
	public List<String> getContinents();
	public List<String> getRegions();
	public List<String> getHeadOfStates();
	public List<String> getGovernmentTypes();

}
