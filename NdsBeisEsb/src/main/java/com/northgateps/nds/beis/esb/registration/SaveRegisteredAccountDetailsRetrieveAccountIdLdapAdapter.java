package com.northgateps.nds.beis.esb.registration;

import com.northgateps.nds.beis.api.BeisAccountDetails;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;

/**
 * 
 * Class to retrieve the party ref from ldap when saving registered account details
 *
 */
public class SaveRegisteredAccountDetailsRetrieveAccountIdLdapAdapter extends RetrieveAccountIdLdapAdapter<SaveRegisteredAccountDetailsNdsRequest, SaveRegisteredAccountDetailsNdsResponse> {
                    
    /**
     * Return success on the response as the LDAP request must have worked.
     */
    @Override
    protected SaveRegisteredAccountDetailsNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {

        //If we get this far then we have succeeded
        SaveRegisteredAccountDetailsNdsResponse ndsResponse = new SaveRegisteredAccountDetailsNdsResponse();
        ndsResponse.setSuccess(true);
        return ndsResponse;    
    }

    @Override
    protected String getRequestClassName() {
        return SaveRegisteredAccountDetailsNdsRequest.class.getName();
    }

	@Override
	protected String getUsername(SaveRegisteredAccountDetailsNdsRequest request) {
		return request.getRegisteredAccountDetails().getUpdateUserDetails().getUsername(); 
        
	}

	@Override
	protected String getTenant(SaveRegisteredAccountDetailsNdsRequest request) {
		return request.getTenant();
	}

	@Override
	protected void setAccountId(SaveRegisteredAccountDetailsNdsRequest request, String accountId) {
		
		//Ensure we have an account details object
        if(request.getRegisteredAccountDetails().getAccountDetails() == null){
            request.getRegisteredAccountDetails().setAccountDetails(new BeisAccountDetails());
        }
        
        //The user id has been set so update the request with it.
        request.getRegisteredAccountDetails().getAccountDetails().setAccountId(accountId);		
	}
}