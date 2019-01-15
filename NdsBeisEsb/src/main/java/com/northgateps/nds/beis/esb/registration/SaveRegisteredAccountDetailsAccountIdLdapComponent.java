package com.northgateps.nds.beis.esb.registration;

import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;

/**
 * The purpose of this adapter is to pass a strongly typed request and response class to the 
 * abstract class to save the accountid exchange property into ldap.
 * 
 * @param <TRequest> This must be a request class that implements the BeisPartyRequest interface
 * @param <TResponse> This must implement the NdsResponse interface
 */
public class SaveRegisteredAccountDetailsAccountIdLdapComponent extends
        SaveAccountIdLdapComponent<SaveRegisteredAccountDetailsNdsRequest, SaveRegisteredAccountDetailsNdsResponse> {

    @Override
    protected SaveRegisteredAccountDetailsNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {

        SaveRegisteredAccountDetailsNdsResponse response = new SaveRegisteredAccountDetailsNdsResponse();
        response.setSuccess(true);
        return response;
        
    }

    @Override
    protected String getRequestClassName() {
        return SaveRegisteredAccountDetailsNdsRequest.class.getName();
    }

}
