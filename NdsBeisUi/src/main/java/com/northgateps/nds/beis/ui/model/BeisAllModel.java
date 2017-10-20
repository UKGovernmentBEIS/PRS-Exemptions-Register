package com.northgateps.nds.beis.ui.model;

import org.springframework.web.bind.WebDataBinder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.northgateps.nds.beis.api.BeisAddressDetail;
import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.BeisRegistrationEsbModel;
import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.beis.api.ExemptionType;
import com.northgateps.nds.beis.api.ExemptionTypeEsbModel;
import com.northgateps.nds.beis.api.dashboard.DashboardDetails;
import com.northgateps.nds.platform.application.api.metadata.GenericFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.js.AddressConsolidationJoiner;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.controller.ModelSerializerParser;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.ui.model.NdsUiData;
import com.northgateps.nds.platform.ui.model.TenantSource;
import com.northgateps.nds.platform.ui.model.ViewStateContainer;

/**
 * The entire persisted model of the BEIS application.
 */
public class BeisAllModel extends AbstractNdsMvcModel implements BeisRegistrationEsbModel, ExemptionTypeEsbModel {

    @SuppressWarnings("unused")
    private static final NdsLogger logger = NdsLogger.getLogger(BeisAllModel.class);

    /* The serialiser/parser to convert the model to a string and back again */
    @JsonIgnore
    private transient ModelSerializerParser<BeisViewStateContainer> modelSerializerParser;

    /* The user supplied data relevant for the application */
    private ExemptionDetail exemptionDetails = null;

    /* The model data as it was before the last web page */
    @JsonIgnore
    private transient BeisAllModel viewStateModel = null;

    private BeisRegistrationDetails beisRegistrationDetails = null;

    /*used for modifying the account contact address*/
    @GenericFieldMetadata(joiner = AddressConsolidationJoiner.class)
    private BeisAddressDetail modifiedContactAddress;
    
    private DashboardDetails dashboardDetails;
    
    /** {@inheritDoc} */
    @Override
    @JsonIgnore // prevents infinite loop
    public BeisViewStateContainer getNdsViewState() {
    	
    	// create container
        BeisViewStateContainer container = new BeisViewStateContainer();
        
        // set generic items
        container.setSiteMap(siteMap);
        container.setUiData((UiData) getUiData());
        container.setTenant(getTenant());
        container.setQuestionsModel(questionsModel);
        
        // set project specific items        
        container.setExemptionDetails(exemptionDetails);
        container.setBeisRegistrationDetails(beisRegistrationDetails);

        return container;
    }

    /** {@inheritDoc} */
    @Override
    public void setNdsViewState(ViewStateContainer viewStateContainer) {
    	
    	// cast it
        BeisViewStateContainer peViewStateContainer = ((BeisViewStateContainer) viewStateContainer);
        
        // set generic items
        setUiData(peViewStateContainer.getUiData());
        setQuestionsModel(peViewStateContainer.getQuestionsModel());
        setTenant(peViewStateContainer.getTenant());
        
        // set project specific items
        setExemptionDetails(peViewStateContainer.getExemptionDetails());
        setBeisRegistrationDetails(peViewStateContainer.getBeisRegistrationDetails());
    }

    /**
     * Adds system defined values to the model.
     */
    @Override
    public void populateDefaults() {
    }
        
    @Override
    protected NdsUiData constructUiData() {
        return new UiData();
    }

    @Override
    public void createModelSerializerParser() {
        modelSerializerParser = new ModelSerializerParser<BeisViewStateContainer>(getSecurityKeySourceFactory());
    }

    @Override
    public ModelSerializerParser<?> getModelSerializerParser() {
        return modelSerializerParser;
    }

    @Override
    public Class<? extends ViewStateContainer> getViewStateContainerClass() {
        return BeisViewStateContainer.class;
    }
    
    @Override
    protected TenantSource getDefaultTenant() {
        return new TenantSource("BEIS", true);
    }

    public void registerFieldCustomEditors(WebDataBinder binder) {

    }

    public void setForce(String id) {

    }

    @Override
    public BeisRegistrationDetails getBeisRegistrationDetails() {
        return beisRegistrationDetails;
    }

    public void setBeisRegistrationDetails(BeisRegistrationDetails beisRegistrationDetails) {
        this.beisRegistrationDetails = beisRegistrationDetails;
    }

    public ExemptionDetail getExemptionDetails() {
        return exemptionDetails;
    }

    public void setExemptionDetails(ExemptionDetail exemptionDetails) {
        this.exemptionDetails = exemptionDetails;
    }

    public BeisAddressDetail getModifiedContactAddress() {
		return modifiedContactAddress;
	}

	public void setModifiedContactAddress(BeisAddressDetail modifiedContactAddress) {
		this.modifiedContactAddress = modifiedContactAddress;
	}

    @Override
    public ExemptionType  getExemptionType() {
        return ((UiData)getUiData()).getSelectedExemptionTypeText();
    }

    public DashboardDetails getDashboardDetails() {    	
    	return dashboardDetails;
	}

	public void setDashboardDetails(DashboardDetails dashboardDetails) {
		this.dashboardDetails = dashboardDetails;
	}

	@Override
	public void clear() {
	    BeisViewStateContainer emptyViewStateContainer = new BeisViewStateContainer();
        
        // keep the current tenant but clear the viewstate
        emptyViewStateContainer.setTenant(this.tenant);
        this.setNdsViewState(emptyViewStateContainer);
        
        // clear UI data
        this.setUiData(new UiData());
        
        // clear other bits and pieces
        this.questionsModel = null;
	}
}
