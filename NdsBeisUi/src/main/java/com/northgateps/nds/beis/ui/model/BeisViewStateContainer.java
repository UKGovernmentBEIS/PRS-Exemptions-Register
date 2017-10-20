package com.northgateps.nds.beis.ui.model;

import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.platform.ui.model.NdsUiData;
import com.northgateps.nds.platform.ui.model.ViewStateContainer;

/**
 * A container for just the data of the model that is to be persisted between
 * view pages
 */
public class BeisViewStateContainer extends ViewStateContainer {

	private NdsUiData uiData;
	private ExemptionDetail exemptionDetails;
	private String tenant;
	private String forceName;
	private BeisRegistrationDetails beisRegistrationDetails;

	public NdsUiData getUiData() {
		return uiData;
	}

	public void setUiData(UiData uiData) {
		this.uiData = uiData;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getForceName() {
		return forceName;
	}

	public void setForceName(String forceName) {
		this.forceName = forceName;
	}

	public ExemptionDetail getExemptionDetails() {
		return exemptionDetails;
	}

	public void setExemptionDetails(ExemptionDetail exemptionDetails) {
		this.exemptionDetails = exemptionDetails;
	}

	public BeisRegistrationDetails getBeisRegistrationDetails() {
		return beisRegistrationDetails;
	}

	public void setBeisRegistrationDetails(BeisRegistrationDetails beisRegistrationDetails) {
		this.beisRegistrationDetails = beisRegistrationDetails;
	}

}