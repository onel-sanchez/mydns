/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydns;

/**
 *
 * @author One
 */
public class RR {
    String ipAddress;
    String domain;

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getDomain() {
        return domain;
    }

    public RR(String serverName, String domain) {
        this.ipAddress = serverName;
        this.domain = domain;
    }
}
