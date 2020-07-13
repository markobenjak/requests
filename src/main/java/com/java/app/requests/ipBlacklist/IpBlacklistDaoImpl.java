package com.java.app.requests.ipBlacklist;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class IpBlacklistDaoImpl implements IpBlacklistDao {

	
	public IpBlacklistDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;  
	}  

	NamedParameterJdbcTemplate template;
	
	@Override
	public List<IpBlacklist> findAll() {
		return template.query("select * from ip_blacklist", new IpBlacklistRowMapper());
	}


}
