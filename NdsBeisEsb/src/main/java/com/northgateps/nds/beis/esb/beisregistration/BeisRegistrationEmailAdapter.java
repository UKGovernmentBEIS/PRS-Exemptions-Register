package com.northgateps.nds.beis.esb.beisregistration;

import java.util.ArrayList;
import java.util.HashMap;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsRequest;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsEmailAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Adapter class that processes the registration email between native (REST) objects and email server
 * 
 */
public class BeisRegistrationEmailAdapter
        extends NdsEmailAdapter<BeisRegistrationNdsRequest, BeisRegistrationNdsResponse> {

    final static ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();
    protected final NdsLogger logger = NdsLogger.getLogger(getClass());
    public static final String EMAIL_CONFIGURATION_FILE=configurationManager.getString("email.templatename");

    /**
     * Sets email configuration values
     * 
     * @return Hash Map Contains dynamic values to be replaced with place holders in Email Template at runtime
     */
    @Override
    public HashMap<String, Object> doEmailRequestProcess(BeisRegistrationNdsRequest ndsRequest,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) {
        ArrayList<String> toList = new ArrayList<String>();
        toList.add(ndsRequest.getRegistrationDetails().getUserDetails().getEmail());

        // save the request with the activation code for later processing
        ndsExchange.setRequestMessageBody(ndsRequest);
        ndsExchange.storeOriginalMessageAsString();
        
        HashMap<String, Object> emailConfigMap = new HashMap<String, Object>();
        emailConfigMap.put("type", "RegistrationEmailTemplate");
        emailConfigMap.put("language", "en");
        emailConfigMap.put("to", toList);
        emailConfigMap.put("name", ndsRequest.getRegistrationDetails().getUserDetails().getUsername());
        emailConfigMap.put("activationCode", ndsRequest.getRegistrationDetails().getActivationCode());
        emailConfigMap.put("registrationActivation",
                generateEmailBody(ndsRequest.getRegistrationDetails().getActivationCode(),
                        ndsRequest.getRegistrationDetails().getTenant()));
        emailConfigMap.put("activationText",
                generateEmailBodyActivationText(ndsRequest.getRegistrationDetails().getActivationCode(),
                        ndsRequest.getRegistrationDetails().getTenant()));
        emailConfigMap.put("emailTemplateFile",EMAIL_CONFIGURATION_FILE);
        
        return emailConfigMap;
    }

    /**
     * Constructs links for email body
     * 
     * @param code and tenant for link
     * 
     * @return email body as String
     */
    private String generateEmailBody(String activationCode, String tenant) {

        StringBuilder body = new StringBuilder();

        String activationUrl = configurationManager.getString("registration.activationUrl");

        String emailBody = body.append(activationUrl + "?tenant=" + tenant
                + "&activateRegistrationDetails.activationCode=" + activationCode).append(
                        System.getProperty("line.separator")).toString();
        String bodyText = "<a href=\"" + emailBody + "\">"+configurationManager.getString("registrationLinkLabel")+"</a>";

        return bodyText;

    }

    private String generateEmailBodyActivationText(String activationCode, String tenant) {

        StringBuilder body = new StringBuilder();

        String activationUrl = configurationManager.getString("registration.activationUrl");

        body.append(activationUrl + "?tenant=" + tenant);

        body.append(System.getProperty("line.separator"));

        return body.append(System.getProperty("line.separator")).toString();

    }

    
     /**
     * Create an NDS response that indicates successfully sent an email
     * 
     * @param exchange
     * @throws NdsApplicationException
     */

    @Override
    protected BeisRegistrationNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {

        BeisRegistrationNdsResponse response = new BeisRegistrationNdsResponse();

        response.setSuccess(true);

        return response;

    }

    @Override
    public String getRequestClassName() {
        return BeisRegistrationNdsRequest.class.getName();
    }

    @Override
    public String getResponseClassName() {
        return BeisRegistrationNdsResponse.class.getName();
    }

}
