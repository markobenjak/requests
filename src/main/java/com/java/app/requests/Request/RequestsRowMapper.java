package com.java.app.requests.Request;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestsRowMapper implements RowMapper<Requests> {

	@Override
	public Requests mapRow(ResultSet rs, int arg1) throws SQLException {
		Requests requests = new Requests();

		requests.setCustomerID(rs.getInt("customerID"));
		requests.setTagID(rs.getInt("tagID"));
		requests.setUserID(rs.getString("userID"));
		requests.setRemoteIP(rs.getString("remoteIP"));
		requests.setTimestamp(rs.getDate("timestamp"));
		return requests;

	}

}
