package com.northgateps.nds.beis.esb.registration;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;

import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse;
import com.northgateps.nds.platform.api.passwordreset.PasswordResetNdsRequest;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxyImpl;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * This extracts the email address from retrieveRegisteredDetailsNdsResponse,passes it further in request to
 * PasswordResetNdsRequest
 */
public class GetUserEmailAddressAdapter {

    public static String UPDATED_EMAIL_ADDRESS = "userEmailAddress";
    public static String IS_UPDATE_REQUIRED = "isUpdateRequired";
    
    protected final NdsLogger logger = NdsLogger.getLogger(getClass());

    // method to convert PasswordResetNdsRequest into RetrieveRegisteredDetailsNdsRequest
    public RetrieveRegisteredDetailsNdsRequest createMyAccountDetailsRequest(Exchange exchange) {
        PasswordResetNdsRequest ndsRequest = (PasswordResetNdsRequest) exchange.getIn().getBody();
        RetrieveRegisteredDetailsNdsRequest retrieveRegisteredDetailsNdsRequest = new RetrieveRegisteredDetailsNdsRequest();

        retrieveRegisteredDetailsNdsRequest.setUsername(ndsRequest.getPasswordResetDetails().getUsername());
        retrieveRegisteredDetailsNdsRequest.setTenant(ndsRequest.getPasswordResetDetails().getTenant());

        return retrieveRegisteredDetailsNdsRequest;

    }

    /** Method which compares the email address from backoffice with the one in ldap and if different then it
     *  sets in the exchange and later updates it in ldap 
     */
    public PasswordResetNdsRequest getEmailAddress(Exchange exchange) throws NdsApplicationException {
        NdsSoapRequestAdapterExchangeProxyImpl ndsExchange = new NdsSoapRequestAdapterExchangeProxyImpl(exchange);
        PasswordResetNdsRequest request = (PasswordResetNdsRequest) ndsExchange.getOriginalRequestMessage().getBody();

        try {
            RetrieveRegisteredDetailsNdsResponse ndsResponse = (RetrieveRegisteredDetailsNdsResponse) exchange.getIn().getBody();
            String userEmailAddressFromBackOffice = ndsResponse.getBeisRegistrationDetails().getUserDetails().getEmail();

            String userEmailAddressFromLdap = (String) ndsExchange.getAnExchangeProperty("userEmailAddress");

            if (!StringUtils.isEmpty(userEmailAddressFromBackOffice) && !StringUtils.isEmpty(userEmailAddressFromLdap)) {
                
                /*check if userEmailAddressFromBackOffice and userEmailAddressFromLdap are not same, which means
                update is required in ldap*/
                if (!userEmailAddressFromBackOffice.equals(userEmailAddressFromLdap)) {

                    ndsExchange.setAnExchangeProperty(UPDATED_EMAIL_ADDRESS, userEmailAddressFromBackOffice);
                    ndsExchange.setAnExchangeProperty(IS_UPDATE_REQUIRED, "true");
                }
            } else {
                logger.error("Either email address from ldap or email address from backoffice not found");
            }

        }

        catch (Exception e) {
            throw new NdsApplicationException(
                    "Error occured during retrieving get registered details reponse:  " + e.getMessage(), e);
        }
        return request;
    }

}
