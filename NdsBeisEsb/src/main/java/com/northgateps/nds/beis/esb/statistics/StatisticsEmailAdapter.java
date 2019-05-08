package com.northgateps.nds.beis.esb.statistics;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.northgateps.nds.beis.api.statistics.ExemptionInformation;
import com.northgateps.nds.beis.api.statistics.StatisticsNdsRequest;
import com.northgateps.nds.beis.api.statistics.StatisticsNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsEmailAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Adapter class that processes the exemption statistics request/response email between native (REST) objects and email
 * server 
 */
public class StatisticsEmailAdapter
        extends NdsEmailAdapter<StatisticsNdsRequest, StatisticsNdsResponse> {

    final static ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();
    public static final String EMAIL_CONFIGURATION_FILE=configurationManager.getString("email.templatename");
    /**
     * Sets email configuration values
     * 
     * @return Map contains dynamic values to be replaced with place holders in Email Template at runtime
     */
    @Override
    public Map<String, Object> doEmailRequestProcess(StatisticsNdsRequest request,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) {

        List<String> toList = Arrays.asList(
                ConfigurationFactory.getConfiguration().getString("admin.email.address").split("\\s*,\\s*"));
        Map<String, Object> emailConfigMap = new HashMap<String, Object>();
        emailConfigMap.put("type", "StatisticsTemplate");
        emailConfigMap.put("to", toList);
        emailConfigMap.put("submittedExemptionsCount", request.getNumberOfRegisteredExemptions());     
        emailConfigMap.put("domesticExemptionCount", request.getNumberOfDomesticExemptions());
        emailConfigMap.put("nonDomesticExemptionCount", request.getNumberOfNonDomesticExemptions());
        
        if (request.getFromDateTime() != null && request.getToDateTime() != null ) {
            emailConfigMap.put("fromDateTime",
                    request.getFromDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            emailConfigMap.put("toDateTime",
                    request.getToDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }
        emailConfigMap.put("successfulLoginsCount", request.getNumberOfSuccessfulLogins());
        emailConfigMap.put("failedLoginsCount", request.getNumberOfFailedLogins());
        emailConfigMap.put("emailTemplateFile",EMAIL_CONFIGURATION_FILE);
        
        if(request.getNumberOfDomesticExemptions() > 0) {
            emailConfigMap.put("domesticExemptions" ,  convertExemptionInfo(request.getDomesticExemptions()));
        }
        if(request.getNumberOfNonDomesticExemptions() > 0) {
            emailConfigMap.put("nonDomesticExemptions" ,  convertExemptionInfo(request.getNonDomesticExemptions()));
        }
        
        emailConfigMap.put("registrationCount", request.getNumberOfRegistration());
        emailConfigMap.put("changePasswordCount", request.getNumberOfChangePassword());
        emailConfigMap.put("resetPasswordCount", request.getNumberOfResetPassword());
        
        return emailConfigMap;
    }  
   
    
    private String convertExemptionInfo(List<ExemptionInformation> list) {
        StringBuilder refsOnLines = new StringBuilder();        
        for (ExemptionInformation info : list) {
            String exemptiontype=info.getExemptionType().replace("<br>", "");
            refsOnLines.append(info.getExemptionsReferences() + "- " + exemptiontype);
            refsOnLines.append("<br/>");            
        }
        return refsOnLines.toString();
    }

    
    /**
     * Create an NDS response that indicates successfully sent an email
     * 
     * @param exchange
     * @throws NdsApplicationException
     */

    @Override
    protected StatisticsNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {

        StatisticsNdsResponse response = new StatisticsNdsResponse();

        response.setSuccess(true);

        return response;

    }

    @Override
    public String getRequestClassName() {
        return StatisticsNdsRequest.class.getName();
    }

    @Override
    public String getResponseClassName() {
        return StatisticsNdsResponse.class.getName();
    }

}
