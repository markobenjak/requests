package com.java.app.requests.ipBlacklist;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IpBlacklistRowMapper implements RowMapper<IpBlacklist> {

	@Override
	public IpBlacklist mapRow(ResultSet rs, int arg1) throws SQLException {
		IpBlacklist ipBlacklist = new IpBlacklist();

		ipBlacklist.setIp(rs.getLong("ip"));

		return ipBlacklist;

	}

}
