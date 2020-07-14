package com.java.app.requests.Request;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class RequestsDaoImpl implements RequestsDao {

	
	public RequestsDaoImpl(NamedParameterJdbcTemplate template, JdbcTemplate jdbcTemplate) {
        this.template = template;
        this.jdbcTemplate = jdbcTemplate;
	}  

	NamedParameterJdbcTemplate template;
	JdbcTemplate jdbcTemplate;


	@Override
	public String createRequests(Requests requests) {
		boolean valid = validateRequests(requests) ;
		String returnString = "";
		KeyHolder holder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO incoming_requests(customerid, tagid, userid, remoteip, timestamp) "
				+ "VALUES(:customerid,:tagid,:userid,:remoteip,:timestamp);";

		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("customerid", requests.getCustomerID())
				.addValue("tagid", requests.getTagID())
				.addValue("userid", requests.getUserID())
				.addValue("remoteip", requests.getRemoteIP())
				.addValue("timestamp", requests.getTimestamp());
		template.update(sql,param, holder);

		final String ipBlacklistSql = "SELECT ip from ip_blacklist;";
		List<String> listOfIpBlacklist = jdbcTemplate.queryForList(ipBlacklistSql,String.class);

		final String listOfCustomersSql = "SELECT id FROM customer;";
		List<Integer> listOfCustomer = jdbcTemplate.queryForList(listOfCustomersSql,Integer.class);

		if(!listOfCustomer.contains(requests.getCustomerID())) {
			returnString = returnString + "Invalid Customer. ";
			valid = false;
			return returnString;
		}
		final String userActivitySql = "SELECT active FROM customer WHERE id = ?";
		Integer userActivity = jdbcTemplate.queryForObject(userActivitySql, Integer.class, new Object[]{requests.getCustomerID()});

		final String listOfUaSql = "SELECT ua FROM ua_blacklist;";
		List<String> listOfUa = jdbcTemplate.queryForList(listOfUaSql,String.class);

		if(!valid)
			returnString = returnString + "Request missing data or wronf data type. ";
		else if(listOfIpBlacklist.contains(requests.getRemoteIP().replace(".", ""))) {
			returnString = returnString + "Incoming Ip blacklisted. ";
			valid = false;
		}else if(listOfUa.contains(requests.getUserID())){
			returnString = returnString + "User blacklisted ";
			valid = false;
		}else if(userActivity == null || userActivity == 0){
			returnString = returnString + "Customer not Active ";
			valid = false;
		}else{
			returnString = returnString + "Request valid";
		}

		returnString = returnString + storeStats(requests, valid);
		return returnString;
	}

	public boolean validateRequests(Requests requests){
		boolean valid = true;

		if (requests.getCustomerID() == null)
		 	valid = false;
		else if(!requests.getCustomerID().getClass().equals(Integer.class))
			valid = false;
		 else if(requests.getRemoteIP() == null)
		 	valid = false;
		else if(!requests.getRemoteIP().getClass().equals(String.class))
			valid = false;
		 else if(requests.getTagID() == null)
		 	valid = false;
		else if(!requests.getTagID().getClass().equals(Integer.class))
			valid = false;
		 else if(requests.getTimestamp() == null)
		 	valid = false;
		else if(!requests.getTimestamp().getClass().equals(Date.class))
			valid = false;
		 else if(requests.getUserID() == null)
		 	valid = false;
		 else if(!requests.getUserID().getClass().equals(String.class))
		 	valid = false;

		return valid;
	}

	public String storeStats(Requests requests, boolean valid){
		LocalDateTime now = LocalDateTime.now();
		String returnString = "";

		final String checkIfCustomerTimeExsistsSql = "SELECT id, request_count, invalid_count FROM hourly_stats "
				+ "WHERE customer_id = ? "
				+ "AND time > NOW() - interval ' "
				+ LocalDateTime.now().getMinute()
				+" minutes'";

		Map<String, Object> customerResult = null;
		try {
			customerResult = (Map<String, Object>) jdbcTemplate
					.queryForMap(checkIfCustomerTimeExsistsSql, new Object[]{requests.getCustomerID()});
		}catch(Exception e){
			returnString = returnString + e;
		}

		try {
			if (customerResult != null && valid) {

				final String updateHourlyStats = "UPDATE hourly_stats " +
						"SET time = NOW(), request_count = "
						+ customerResult.get("request_count")
						+ "+1 WHERE id = "
						+ customerResult.get("id") + "";
				jdbcTemplate.update(updateHourlyStats);

			} else if (customerResult != null && !valid) {

				final String updateHourlyStats = "UPDATE hourly_stats " +
						"SET time = NOW(), invalid_count = "
						+ customerResult.get("invalid_count")
						+ "+1 WHERE id = "
						+ customerResult.get("id") + "";
				jdbcTemplate.update(updateHourlyStats);

			} else if (customerResult == null && valid) {

				final String insertHourlyStats = "INSERT INTO hourly_stats(customer_id, time, request_count, invalid_count) "
						+ "VALUES ("
						+ requests.getCustomerID()
						+ " ,'"
						+ requests.getTimestamp()
						+ "', 1, 0)";
				jdbcTemplate.update(insertHourlyStats);

			} else if (customerResult == null && !valid) {

				final String insertHourlyStats = "INSERT INTO hourly_stats(customer_id, time, request_count, invalid_count) "
						+ "VALUES ("
						+ requests.getCustomerID()
						+ " ,'"
						+ requests.getTimestamp()
						+ "', 0, 1)";
				jdbcTemplate.update(insertHourlyStats);

			}
		}catch(Exception e){
			returnString = returnString + e;
		}
//		Long i = new Date().getTime(); //get date in LONG to know value for JSON
//		System.out.println(i);

		return returnString;
	}

	public List<Requests> findAll() {
		return template.query("select * from incoming_requests", new RequestsRowMapper());
	}

	@Override
	public String statisticPerCustomer(int customerId) {
		String returnString = "";
		final String checkIfCustomerTimeExsistsSql = "SELECT SUM(request_count) as valid, SUM(invalid_count) as invalid FROM hourly_stats "
				+ "WHERE customer_id = ? "
				+ "AND time BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW()";

		Map<String, Object> customerStatistic = null;
		try {
			customerStatistic = (Map<String, Object>) jdbcTemplate
					.queryForMap(checkIfCustomerTimeExsistsSql, new Object[]{customerId});
		}catch(Exception e){
			returnString = returnString + e;
		}

		if(customerStatistic != null){
			BigDecimal validRequests = customerStatistic.get("valid") == null ? BigDecimal.valueOf(0) : (BigDecimal) customerStatistic.get("valid");
			BigDecimal invalidRequests = customerStatistic.get("invalid") == null ? BigDecimal.valueOf(0) : (BigDecimal) customerStatistic.get("invalid");

			returnString = returnString + " Customer had "
					+ validRequests
					+ " valid requests and "
					+ invalidRequests
					+ " invalid requests. In total " + validRequests.add(invalidRequests) + " requests.";
		}
		return returnString;
	}

	public String statisticPerDay(String day) {
		String returnString = "";
		final String StatsPerDay = "SELECT SUM(request_count) as valid, SUM(invalid_count) as invalid FROM hourly_stats "
				+ "WHERE time between '" + day + "' and '"+ day +" 23:59:59' ";

		Map<String, Object> customerStatisticDay = null;
		try {
			customerStatisticDay = (Map<String, Object>) jdbcTemplate
					.queryForMap(StatsPerDay);
		}catch(Exception e){
			returnString = returnString + e;
		}

		if(customerStatisticDay != null){
			BigDecimal validRequests = customerStatisticDay.get("valid") == null ? BigDecimal.valueOf(0) : (BigDecimal) customerStatisticDay.get("valid");
			BigDecimal invalidRequests = customerStatisticDay.get("invalid") == null ? BigDecimal.valueOf(0) : (BigDecimal) customerStatisticDay.get("invalid");

			returnString = returnString + " Number of requests on "
					+ day
					+ " were: "
					+ validRequests
					+ " valid requests and "
					+ invalidRequests
					+ " invalid requests. In total " + validRequests.add(invalidRequests) + " requests.";
		}
		return returnString;
	}

	@Override
	public void deleteStoredRequests() {
		final String deleteSql = "DELETE FROM incoming_requests WHERE timestamp < NOW() - INTERVAL '1 day'";

		template.execute(deleteSql,new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				return ps.executeUpdate();
			}
		});
	}
}
