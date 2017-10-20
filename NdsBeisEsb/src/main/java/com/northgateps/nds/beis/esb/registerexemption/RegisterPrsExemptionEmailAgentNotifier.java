package com.northgateps.nds.beis.esb.registerexemption;

import java.util.HashMap;
import java.util.NoSuchElementException;

import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsRequest;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsEmailAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Sends an email to landlord email address after registering an exemption
 * 
 */
public class RegisterPrsExemptionEmailAgentNotifier
        extends NdsEmailAdapter<RegisterPrsExemptionNdsRequest, RegisterPrsExemptionNdsResponse> {

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());
    static ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();
    public static final String EMAIL_CONFIGURATION_FILE = configurationManager.getString("email.templatename");

    /**
     * Sends email to landlord email address
     * 
     * @throws NdsApplicationException
     */
    @Override
    public HashMap<String, Object> doEmailRequestProcess(RegisterPrsExemptionNdsRequest request,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) {
        logger.info("Started setting parameters in register exemption by agent email template ");
        HashMap<String, Object> emailConfigMap = new HashMap<String, Object>();

        // set parameters for agent exemption registration email
        emailConfigMap.put("type", "RegisterAgentExemptionEmailTemplate");
        emailConfigMap.put("agentName", ndsExchange.getAnExchangeProperty(RegisterPrsExemptionLookupIdAdapter.AGENT_NAME));
        emailConfigMap.put("agentAddress",ndsExchange.getAnExchangeProperty(RegisterPrsExemptionLookupIdAdapter.AGENT_ADDRESS));
        emailConfigMap.put("maxValueForExemptionType", "£" + request.getMaxPenaltyValue());
        emailConfigMap.put("to",request.getRegisterPrsExemptionDetails().getExemptionDetails().getLandlordDetails().getEmailAddress());
        emailConfigMap.put("exemptionReference",request.getRegisterPrsExemptionDetails().getExemptionDetails().getReferenceId());
        emailConfigMap.put("exemptionType", request.getPwsText());
        emailConfigMap.put("propertyAddress",request.getRegisterPrsExemptionDetails().getExemptionDetails().getEpc().getPropertyAddress().getSingleLineAddressPostcode());
        emailConfigMap.put("emailTemplateFile", EMAIL_CONFIGURATION_FILE);

        try {
            emailConfigMap.put("url", generateEmailBodyText(request.getTenant()));
        } catch (NoSuchElementException e) {
            logger.info("SignUrl in property is not used in the Application.");
        }
        logger.info("Successfully set parameters in register exemption by agent email template");
        return emailConfigMap;
    }

    
    /**
     * Returns success response to the route.
     * 
     */
    @Override
    protected RegisterPrsExemptionNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {
        // Response will always be successful at this point and this condition is checked in the camel route.
        RegisterPrsExemptionNdsResponse response = new RegisterPrsExemptionNdsResponse();

        response.setSuccess(true);
        logger.info("Successfully created the NDS response");
        return response;

    }

    /**
     * Called from the base-class to de-serialize the incoming request.
     * 
     * See this:
     * http://stackoverflow.com/questions/3403909/get-generic-type-of-class
     * -at-runtime
     */
    @Override
    public String getRequestClassName() {
        return RegisterPrsExemptionNdsRequest.class.getName();
    }

    @Override
    public String getResponseClassName() {
        return RegisterPrsExemptionNdsResponse.class.getName();
    }

    /**
     * Constructs links for email body
     * 
     * @param tenant for link
     * 
     * @return email body as String
     */
    private String generateEmailBodyText(String tenant) {
        StringBuilder body = new StringBuilder();
        String website = ConfigurationFactory.getConfiguration().getString("website");
        String url = configurationManager.getString("signin.url");
        String emailBody = body.append(url + "?tenant=" + tenant).toString();
        String bodyText = "<a href=\"" + emailBody + "\">" + website + "</a>";
        return bodyText;
    }
}
