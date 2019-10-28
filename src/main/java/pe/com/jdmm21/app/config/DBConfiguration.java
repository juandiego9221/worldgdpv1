package pe.com.jdmm21.app.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

import pe.com.jdmm21.app.model.Country;

@Configuration
public class DBConfiguration {
	@Value("${jdbcUrl}")
	String jdbcUrl;
	@Value("${dataSource.user}")
	String username;
	@Value("${dataSource.password}")
	String password;
	@Value("${dataSourceClassName}")
	String className;
	
	@Bean
	public DataSource getDatasource() {
		HikariDataSource ds =new HikariDataSource();
		ds.setJdbcUrl(jdbcUrl);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setDriverClassName(className);
		return ds;
	}
	
	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(getDatasource());
		return jdbcTemplate;
	}
	
	
}
