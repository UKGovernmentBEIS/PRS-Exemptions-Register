package com.northgateps.nds.beis.api.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.platform.api.NdsRequest;

/**
 * Request that takes criteria object and other details to display the dashboard
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetPRSAccountExemptionsNdsRequest", propOrder = { "username", "tenant", "accountId","exemptionRefNo","userType" })
@XmlRootElement(name = "GetPRSAccountExemptionsNdsRequest")
public class GetPRSAccountExemptionsNdsRequest implements NdsRequest {
	
	@XmlElement(name = "username")
	protected String username;

	@XmlElement(name = "accountId")
	protected String accountId;

	@XmlElement(name = "tenant")
	protected String tenant;
	
	@XmlElement(name = "ExemptionRefNo")
	protected String exemptionRefNo;
	
	private UserType userType;

    
    public UserType getUserType() {
        return userType;
    }

    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}	
	   
    public String getExemptionRefNo() {
        return exemptionRefNo;
    }
    
    public void setExemptionRefNo(String exemptionRefNo) {
        this.exemptionRefNo = exemptionRefNo;
    }

}
