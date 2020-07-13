package com.java.app.requests.Request;

import java.util.List;

public interface RequestsDao {

	String createRequests(Requests requests);
	List<Requests> findAll();
	String statisticPerCustomer(int customerId);
	String statisticPerDay(String day);
    void deleteStoredRequests();
}
