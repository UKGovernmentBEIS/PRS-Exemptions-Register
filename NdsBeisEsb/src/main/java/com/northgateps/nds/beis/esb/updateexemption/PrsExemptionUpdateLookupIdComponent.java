package com.northgateps.nds.beis.esb.updateexemption;

import com.northgateps.nds.beis.api.updateexemptiondetails.UpdateExemptionDetailsNdsRequest;
import com.northgateps.nds.beis.api.updateexemptiondetails.UpdateExemptionDetailsNdsResponse;
import com.northgateps.nds.beis.esb.registration.RetrieveAccountIdLdapComponent;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Class to be used to fetch party id from Ldap, derives from the base class
 * that does all the work
 * 
 */
public class PrsExemptionUpdateLookupIdComponent extends RetrieveAccountIdLdapComponent<UpdateExemptionDetailsNdsRequest, UpdateExemptionDetailsNdsResponse> {

	@SuppressWarnings("unused")
	private static final NdsLogger logger = NdsLogger.getLogger(PrsExemptionUpdateLookupIdComponent.class);
	
	@Override
	protected String getRequestClassName() {
		return UpdateExemptionDetailsNdsRequest.class.getName();
	}

    @Override
    protected String getUsername(UpdateExemptionDetailsNdsRequest request) {
      return request.getUserName();
    }

    @Override
    protected String getTenant(UpdateExemptionDetailsNdsRequest request) {
        return request.getTenant();
    }

    @Override
    protected void setAccountId(UpdateExemptionDetailsNdsRequest request, String accountId) {
        request.setAccountId(accountId);
        
    }

    @Override
    protected UpdateExemptionDetailsNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {
        UpdateExemptionDetailsNdsResponse response = new UpdateExemptionDetailsNdsResponse();
        response.setSuccess(true);
        return response;
    }
  }