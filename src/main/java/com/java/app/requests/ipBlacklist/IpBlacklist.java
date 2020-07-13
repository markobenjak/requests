package com.java.app.requests.ipBlacklist;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class IpBlacklist {

   private @Id Long ip;

    public Long getIp() {
        return ip;
    }

    public void setIp(Long ip) {
        this.ip = ip;
    }
}
