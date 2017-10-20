package com.northgateps.nds.beis.api.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.platform.api.AbstractNdsResponse;

/**
 * 
 * Response contains dashboard details to be shown on dashboard
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetPRSAccountExemptionsNdsResponse", propOrder = { "dashboardDetails","userType" })
@XmlRootElement(name = "GetPRSAccountExemptionsNdsResponse")
public class GetPRSAccountExemptionsNdsResponse extends AbstractNdsResponse {
	private DashboardDetails dashboardDetails;
	
	private UserType userType;
	
	public DashboardDetails getDashboardDetails() {
		return dashboardDetails;
	}

	public void setDashboardDetails(DashboardDetails dashboardDetails) {
		this.dashboardDetails = dashboardDetails;
	}

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

}
