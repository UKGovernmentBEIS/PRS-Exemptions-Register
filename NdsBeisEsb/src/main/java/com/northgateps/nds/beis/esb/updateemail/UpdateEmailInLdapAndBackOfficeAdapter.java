package com.northgateps.nds.beis.esb.updateemail;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;

import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsResponse;
import com.northgateps.nds.platform.api.updateemail.UpdateEmailNdsRequest;
import com.northgateps.nds.platform.esb.adapter.NdsEmailAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxyImpl;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Adapter class that handles preparing the exchange to be passed from saveRegisteredAccountDetailsServiceRoute to
 * updateEmailServiceSubroute.
 *
 * @author Simon.Hogg
 */
public class UpdateEmailInLdapAndBackOfficeAdapter {

    @SuppressWarnings("unused") private static final NdsLogger logger = NdsLogger.getLogger(
            UpdateEmailInLdapAndBackOfficeAdapter.class);

    public static final String EXCHANGE_PROPERTY_UPDATE_EMAIL = "updateEmail";

    public void checkForUpdateEmail(Exchange exchange) throws NdsApplicationException {
        NdsSoapRequestAdapterExchangeProxyImpl ndsExchange = new NdsSoapRequestAdapterExchangeProxyImpl(exchange);

        try {
            SaveRegisteredAccountDetailsNdsRequest ndsRequest =
                    (SaveRegisteredAccountDetailsNdsRequest) ndsExchange.getRequestMessageBody(
                            SaveRegisteredAccountDetailsNdsRequest.class);

            UpdateBeisRegistrationDetails registeredAccountDetails = ndsRequest.getRegisteredAccountDetails();

            // if there is an email address then update it
            if (registeredAccountDetails.getUpdateUserDetails().getUpdatingEmail()) {
                ndsExchange.setAnExchangeProperty(EXCHANGE_PROPERTY_UPDATE_EMAIL, true);
            }
        } catch (ClassNotFoundException e) {
            throw new NdsApplicationException("Unable to find required class: SaveRegisteredAccountDetailsNdsRequest");
        }
    }

    /**
     * Set up the request so that we can send a notification email to the new address.
     * 
     * @param exchange
     * @throws NdsApplicationException
     */
    public void processRequest(Exchange exchange) throws NdsApplicationException {
        NdsSoapRequestAdapterExchangeProxyImpl ndsExchange = new NdsSoapRequestAdapterExchangeProxyImpl(exchange);

        UpdateEmailNdsRequest updateEmailNdsRequest;

        try {
            SaveRegisteredAccountDetailsNdsRequest ndsRequest = (SaveRegisteredAccountDetailsNdsRequest) ndsExchange.getRequestMessageBody(
                    SaveRegisteredAccountDetailsNdsRequest.class);

            ndsExchange.setAnExchangeProperty(NdsEmailAdapter.RESPONSE_TYPE, SaveRegisteredAccountDetailsNdsResponse.class.getName());
            
            // Create the type converter
            TypeConverter converter = ndsExchange.getTypeConverter();
            updateEmailNdsRequest = converter.convertTo(UpdateEmailNdsRequest.class, ndsRequest);
        } catch (Exception e) {
            throw new NdsApplicationException("Error occurred during request conversion process:  " + e.getMessage(), e);
        }

        ndsExchange.setResponseMessageBodyWithHeaders(updateEmailNdsRequest, exchange.getIn().getHeaders());
    }

    /**
     * Create a SaveRegisteredAccountDetailsNdsResponse.
     *
     * @param exchange
     */
    public void processResponse(Exchange exchange) {
        NdsSoapRequestAdapterExchangeProxyImpl ndsExchange = new NdsSoapRequestAdapterExchangeProxyImpl(exchange);
        SaveRegisteredAccountDetailsNdsResponse response = new SaveRegisteredAccountDetailsNdsResponse();

        response.setSuccess(true);

        ndsExchange.setResponseMessageBodyWithHeaders(response, exchange.getIn().getHeaders());
    }

}
