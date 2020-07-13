package com.java.app.requests;

import com.java.app.requests.Request.Requests;
import com.java.app.requests.Request.RequestsDaoImpl;
import com.java.app.requests.ipBlacklist.IpBlacklist;
import com.java.app.requests.ipBlacklist.IpBlacklistDaoImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/")
public class RequestsController {

    @Resource
    IpBlacklistDaoImpl ipBlacklistDaoImpl;
    @Resource
    RequestsDaoImpl requestsDaoImpl;

    @GetMapping(value = "ipBlacklist")
    public List<IpBlacklist> getIpBlacklist() {
        return ipBlacklistDaoImpl.findAll();
    }

    @PostMapping(value = "requests")
    public String createOrder(@RequestBody Requests requests) {
        return requestsDaoImpl.createRequests(requests);
    }

    @GetMapping(value = "requests")
    public List<Requests> getRequests() {
        return requestsDaoImpl.findAll();
    }

    @GetMapping(value = "statisticCustomer/{customerId}")
    public String statisticPerCustomer(@PathVariable("customerId") int customerId) {
        return requestsDaoImpl.statisticPerCustomer(customerId);
    }

    @GetMapping(value = "statisticDay/{day}")
    public String statisticPerDay(@PathVariable("day") String day) {
        return requestsDaoImpl.statisticPerDay(day);
    }
}
