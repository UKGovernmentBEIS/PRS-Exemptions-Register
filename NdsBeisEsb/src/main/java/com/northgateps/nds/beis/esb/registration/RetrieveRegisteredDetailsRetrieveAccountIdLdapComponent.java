package com.northgateps.nds.beis.esb.registration;

import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Class to retrieve the account id from LDAP which extends RetrieveAccountIdLdapComponent
 *
 */
public class RetrieveRegisteredDetailsRetrieveAccountIdLdapComponent extends
        RetrieveAccountIdLdapComponent<RetrieveRegisteredDetailsNdsRequest, RetrieveRegisteredDetailsNdsResponse> {

    private static final NdsLogger logger = NdsLogger.getLogger(
            RetrieveRegisteredDetailsRetrieveAccountIdLdapComponent.class);

    @Override
    protected String getRequestClassName() {
        return RetrieveRegisteredDetailsNdsRequest.class.getName();
    }

    @Override
    protected String getUsername(RetrieveRegisteredDetailsNdsRequest request) {
        return request.getUsername();
    }

    @Override
    protected String getTenant(RetrieveRegisteredDetailsNdsRequest request) {
        return request.getTenant();
    }

    @Override
    protected void setAccountId(RetrieveRegisteredDetailsNdsRequest request, String accountId) {
        request.setAccountId(accountId);
    }

    @Override
    protected RetrieveRegisteredDetailsNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {
       
        logger.info("Started generating response for request made to retreive party ref");
        RetrieveRegisteredDetailsNdsResponse ndsResponse = new RetrieveRegisteredDetailsNdsResponse();
        ndsResponse.setSuccess(true);
        logger.info("Sucessfully returing response for request made to retreive party ref");
        return ndsResponse;
    }
}