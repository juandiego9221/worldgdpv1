package pe.com.jdmm21.app.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LookupDAOImpl implements LookupDAO {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<String> getContinents() {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		return namedParameterJdbcTemplate.queryForList(
				"select distinct continent from country order by continent",
				mapSqlParameterSource, String.class);
	}

	@Override
	public List<String> getRegions() {
		MapSqlParameterSource mapSqlParameterSource =new MapSqlParameterSource();
		return namedParameterJdbcTemplate.queryForList(
				"select distinct region from country order by region"
				, mapSqlParameterSource,String.class);
	}

	@Override
	public List<String> getHeadOfStates() {
		MapSqlParameterSource mapSqlParameterSource =new MapSqlParameterSource();
		return namedParameterJdbcTemplate.queryForList(
				"select distinct headofstate from country order by headofstate"
				, mapSqlParameterSource,String.class);
	}

	@Override
	public List<String> getGovernmentTypes() {
		MapSqlParameterSource mapSqlParameterSource =new MapSqlParameterSource();
		return namedParameterJdbcTemplate.queryForList(
				"select distinct governmentform from country"
				, mapSqlParameterSource,String.class);
	}

}
