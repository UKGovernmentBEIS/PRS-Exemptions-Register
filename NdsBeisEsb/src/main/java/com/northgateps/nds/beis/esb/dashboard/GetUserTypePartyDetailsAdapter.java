package com.northgateps.nds.beis.esb.dashboard;

import org.apache.camel.Exchange;

import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsRequest;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxyImpl;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;

/**
 * This extracts the user type from retrieveRegisteredDetailsNdsResponse,passes it further in request to
 * getPRSAccountExemptions
 */
public class GetUserTypePartyDetailsAdapter {

    public GetPRSAccountExemptionsNdsRequest getUserType(Exchange exchange) throws NdsApplicationException {
        NdsSoapRequestAdapterExchangeProxyImpl ndsExchange = new NdsSoapRequestAdapterExchangeProxyImpl(exchange);
        GetPRSAccountExemptionsNdsRequest request = (GetPRSAccountExemptionsNdsRequest) ndsExchange.getOriginalRequestMessage().getBody();

        try {
            RetrieveRegisteredDetailsNdsResponse ndsResponse = (RetrieveRegisteredDetailsNdsResponse) exchange.getIn().getBody();
            String userType = ndsResponse.getBeisRegistrationDetails().getUserDetails().getUserType().toString();

            if (userType.equals("Landlord")) {
                request.setUserType(UserType.LANDLORD);
            } else {
                request.setUserType(UserType.AGENT);
            }
        }

        catch (Exception e) {
            throw new NdsApplicationException(
                    "Error occured during retrieving get registered details reponse:  " + e.getMessage(), e);
        }
        return request;
    }

    // converts getPRSAccountExemptionsNdsRequest to retrieveRegisteredDetailsNdsRequest to get the user type
    public RetrieveRegisteredDetailsNdsRequest createPartyDetailsRequest(Exchange exchange) {
        GetPRSAccountExemptionsNdsRequest ndsRequest = (GetPRSAccountExemptionsNdsRequest) exchange.getIn().getBody();
        RetrieveRegisteredDetailsNdsRequest retrieveRegisteredDetailsNdsRequest = new RetrieveRegisteredDetailsNdsRequest();

        retrieveRegisteredDetailsNdsRequest.setAccountId(ndsRequest.getAccountId());
        retrieveRegisteredDetailsNdsRequest.setUsername(ndsRequest.getUsername());
        retrieveRegisteredDetailsNdsRequest.setTenant(ndsRequest.getTenant());
        return retrieveRegisteredDetailsNdsRequest;

    }

}
